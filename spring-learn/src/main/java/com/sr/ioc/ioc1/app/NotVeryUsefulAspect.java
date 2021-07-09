package com.sr.ioc.ioc1.app;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NotVeryUsefulAspect {



	@Pointcut("execution(* com.sr.ioc.ioc1.app.service..*.*(..))")
	public void anyPublicOperation() {

	}

	@Before("anyPublicOperation()")
	public void before(){
		System.out.println("************AOP***************");
	}

}
