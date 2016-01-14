package mcuCom;

import java.util.Timer;
import java.util.TimerTask;

public class time_Test {

	public static void main(String[] args) {
		Timer timer = new java.util.Timer();
		timer.scheduleAtFixedRate(new MyTask(), 0, 2000);
	}
}

class MyTask extends TimerTask {
	public void run() {
		System.out.println(System.currentTimeMillis());
	}
}