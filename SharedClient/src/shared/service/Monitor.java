package shared.service;

import shared.interfaces.Monitorable;

public class Monitor extends Thread{

	private final Monitorable monitor;
	private static boolean terminate = false;
	
	public Monitor(Monitorable monitor) {
		this.monitor = monitor;
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		while(!terminate) {
			monitor.monitor();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}
	
	public static void terminate() {
		terminate = true;
	}
}
