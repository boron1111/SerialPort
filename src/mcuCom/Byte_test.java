package mcuCom;

import java.io.IOException;

public class Byte_test {

	public static void main(String[] args) throws IOException {
		byte bytes = -125;
		int result = bytes & 0xff;
		System.out.println("�޷�����: \t" + result);
		System.out.println("2����bitλ: \t" + Integer.toBinaryString(result));
	}
}
