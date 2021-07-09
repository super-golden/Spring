package com.sr.ioc.ioc2.event;

import org.springframework.context.ApplicationEvent;

/**
 * 定义监听事件
 */
public class TestEvent extends ApplicationEvent {

	public String msg;

	public TestEvent(Object source, String msg) {
		super(source);
		this.msg = msg;
	}

	public void print() {
		System.out.println(msg);
	}

}
