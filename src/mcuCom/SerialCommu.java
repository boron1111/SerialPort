package mcuCom;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialCommu {
	InMessageProcessor inMsgProcessor;
	OutputStream out;
	CommPort commPort;

	SerialCommu(InMessageProcessor inMsgProcessor) {
		this.inMsgProcessor = inMsgProcessor;
	}

	void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			System.out.println("Error: Port is currently in use");
		} else {
			commPort = portIdentifier.open(this.getClass().getName(), 2000);

			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				OutputStream out = serialPort.getOutputStream();

				this.out = out;

				serialPort
						.addEventListener(new SerialReader(in, inMsgProcessor));
				serialPort.notifyOnDataAvailable(true);

			} else {
				System.out
						.println("Error: Only serial ports are handled by this example.");
			}
		}
	}

	void close() {
		commPort.close();
	}

	/**
	 * Handles the input coming from the serial port. A new line character is
	 * treated as the end of a block in this example.
	 */
	public class SerialReader implements SerialPortEventListener {
		private InputStream in;
		private InMessageProcessor inMsgProcessor;
		private byte[] buffer = new byte[1280];

		public SerialReader(InputStream in, InMessageProcessor inMsgProcessor) {
			this.in = in;
			this.inMsgProcessor = inMsgProcessor;
		}

		public void serialEvent(SerialPortEvent arg0) {
			int data;

			try {
				int len = 0;
				while ((data = in.read()) > -1) {
					// if (data == '\n') {
					// break;
					// }
					buffer[len++] = (byte) data;
				}
				inMsgProcessor.process(buffer, len);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public void send(byte[] buf) {
		try {
			int c = 0;
			while (buf[c] != '\r') { // \n «10£¨\r «13
				out.write(buf[c]);
				c++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

interface InMessageProcessor {
	public void process(byte[] buffer, int len);
}