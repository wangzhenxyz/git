package com.flyaudio.copyapplication;

public class UserThread implements Runnable {
	private Thread threadName = null;

	// 该类的域和方法
	// 一般包含如下start方法用于启动线程，或者将方法类的代码放在类的构造方法中
	public void start() {
		if (threadName == null) {
			threadName = new Thread(this);
			threadName.start();
		}
	}

	public void stop() {
		threadName = null;
	}

	@Override
	public void run() {
		Thread currentThread = Thread.currentThread();
		while (threadName == currentThread) {
			// 有适当的代码调用threadName.sleep(sleeptime)或threadName.yield()
			// TODO Auto-generated method stub
		}

	}

}
