package com.sr.ioc.ioc3.lookupmethod;

public  abstract class GetBeanTest {

	public void showMe(){
		this.getBean().showMe();
	}

	public abstract User getBean();
}
