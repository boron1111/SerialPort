package mcuCom;

class MyInMessageProcessor implements InMessageProcessor {

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
			System.out.println(value);
		}
	}
}

public class Commu_test_long {
	public static void main(String Args[]) throws InterruptedException {
		InMessageProcessor a = new MyInMessageProcessor();

		SerialCommu serialCommu1 = new SerialCommu(a);
		try {
			serialCommu1.connect("COM5");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] buf = new byte[128];

		int len = -1;
		while (true) {
			try {
				while ((len = System.in.read(buf)) > -1) {
					buf[0] = Byte.valueOf(new String(buf, 0, len - 2));
					buf[1] = 13;
					serialCommu1.send(buf);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
