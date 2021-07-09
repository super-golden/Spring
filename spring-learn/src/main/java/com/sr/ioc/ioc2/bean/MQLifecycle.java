package com.sr.ioc.ioc2.bean;

import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;

public class MQLifecycle implements Lifecycle, SmartLifecycle {

	private volatile boolean running = false;

	public MQLifecycle() {
		System.out.println("MQLifecycle construct");
	}

	@Override
	public void start() {
		System.out.println("MQ start.......");
		running = true;
	}

	@Override
	public void stop() {
		System.out.println("MQ stop.........");
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}
}
