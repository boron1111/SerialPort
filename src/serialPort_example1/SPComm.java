package serialPort_example1;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class SPComm implements Runnable, SerialPortEventListener {

	// ���ϵͳ�п��õ�ͨѶ�˿���
	private static CommPortIdentifier portId;
	private static Enumeration<CommPortIdentifier> portList;
	// ���������
	public static InputStream inputStream;
	public static OutputStream outputStream;
	// RS-232�Ĵ��п�
	public static SerialPort serialPort;
	public static Thread commThread;

	// ��ʼ������
	public static void init() {
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals("COM8")) {
					try {
						serialPort = (SerialPort) portId.open(
								"SerialPort-Test", 2000);
						serialPort.addEventListener(new SPComm());
						serialPort.notifyOnDataAvailable(true);
						/* ���ô���ͨѶ���� */
						serialPort.setSerialPortParams(19200,
								SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);

						outputStream = serialPort.getOutputStream();
						inputStream = serialPort.getInputStream();
					} catch (PortInUseException e) {
						e.printStackTrace();
					} catch (TooManyListenersException e) {
						e.printStackTrace();
					} catch (UnsupportedCommOperationException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * ʵ�ֽӿ�SerialPortEventListener�еķ��� ��ȡ�Ӵ����н��յ�����
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:// ��ȡ�����ڷ�����Ϣ
			int newData = 0;
			int i = 0;

			do {
				try {
					newData = inputStream.read();
					// �����ݽ��д���ʡ��...

					i++;
					if (i == 24) { // ����ʵ���������ʵ���ʱ�����ý�������
						newData = -1;
					}

				} catch (IOException e) {
					return;
				}
			} while (newData != -1);

			serialPort.close();// ����һ��Ҫ��close()�����رմ��ڣ��ͷ���Դ

			break;
		default:
			break;
		}
	}

	// �򴮿ڷ�����Ϣ����
	public static void sendMsg() {
		String msg = "xxxxxxxxxxxxxx";// Ҫ���͵�����
		try {
			outputStream.write(hexStringToBytes(msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public void run() {
		init();
		sendMsg();

	}

}
