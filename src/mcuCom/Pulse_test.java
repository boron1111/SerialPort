package mcuCom;


class MyInMessageProcessor1 implements InMessageProcessor {

	@Override
	public void process(byte[] buffer, int len) {
		System.out.println(System.currentTimeMillis());
	}

}

public class Pulse_test {
	public static void main(String Args[]) throws Exception {
		InMessageProcessor a = new MyInMessageProcessor1();

		(new SerialCommu(a)).connect("COM5");
	}
}
