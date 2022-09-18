package com.sr.lifecycle.beans;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SoyaBean implements InitializingBean, DisposableBean, ApplicationContextAware {

	ApplicationContext applicationContext;

	public SoyaBean() {
		System.out.println("SoyaBean construct.....");
	}

	@Override
	public void destroy() throws Exception {
		System.out.println("SoyaBean destroy.....");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("SoyaBean afterPropertiesSet.....");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
