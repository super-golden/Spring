package com.sr.aop.aop2.testexposeproxy;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AServiceImpl1 implements AService {
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void a() {

		this.b();
//  通过下面两步 同时对a和b方法同时增强
//		 <aop:config expose-proxy="true"/>
//		((AService)AopContext.currentProxy()).b();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void b() {

	}
}
