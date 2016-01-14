package mcuCom;

public class Wetness {
	MyInMessageProcessor a;
	SerialCommu serialCommu1;

	class MyInMessageProcessor implements InMessageProcessor {
		public long value;
		public boolean flag = false;

		@Override
		public void process(byte[] buffer, int len) {
			if (len != 4) {
				System.out.println("read data error");
			} else {
				long r1 = buffer[0] & 0xff;
				long r2 = buffer[1] & 0xff;
				long r3 = buffer[2] & 0xff;
				long r4 = buffer[3] & 0xff;
				long value = (r1 << 24) + (r2 << 16) + (r3 << 8) + r4;
				this.value = value;
				flag = true;
			}
		}
	}

	public long getWetness(double i) {
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

	public Wetness() {
		a = new MyInMessageProcessor();

		serialCommu1 = new SerialCommu(a);
		try {
			serialCommu1.connect("COM5");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void main(String[] arg) {
	// Wetness me = new Wetness();
	// System.out.println(me.getWetness(2));
	// }
}
