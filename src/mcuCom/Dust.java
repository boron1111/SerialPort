package mcuCom;

public class Dust {
	MyInMessageProcessor a;
	SerialCommu serialCommu1;

	class MyInMessageProcessor implements InMessageProcessor {
		public int[] value;
		public boolean flag = false;

		@Override
		public void process(byte[] buffer, int len) {
			value = new int[len];
			for (int i = 0; i < len; i++) {
				value[i] = (int) (buffer[i] & 0xff);
			}
			flag = true;
		}
	}

	public int[] getDust(double i) {
		byte[] buf = new byte[2];
		buf[0] = (byte) i;
		buf[1] = 13;
		serialCommu1.send(buf);
		while (!a.flag) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		a.flag = false;
		return a.value;
	}

	public void closeConnect() {
		serialCommu1.close();
	}

	public Dust() {
		a = new MyInMessageProcessor();

		serialCommu1 = new SerialCommu(a);
		try {
			serialCommu1.connect("COM5");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
