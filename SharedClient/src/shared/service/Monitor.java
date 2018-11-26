package shared.service;

import shared.interfaces.Monitorable;

public class Monitor extends Thread{

	private final Monitorable monitor;

	public Monitor(Monitorable monitor) {
		this.monitor = monitor;
		setDaemon(true);
		start();
	}

	@Override
	public void run() {
		while(true) {
			monitor.monitor();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {e.printStackTrace();}
		}
	}

}
