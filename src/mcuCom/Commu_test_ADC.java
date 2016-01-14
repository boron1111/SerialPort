package mcuCom;

class MyInMessageProcessor2 implements InMessageProcessor {

	@Override
	public void process(byte[] buffer, int len) {
		for (int i = 0; i < len; i++) {
			System.out.println((int) buffer[i] & 0xff);
		}
	}
}

public class Commu_test_ADC {

	public static void main(String[] args) {
		InMessageProcessor a = new MyInMessageProcessor2();

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
