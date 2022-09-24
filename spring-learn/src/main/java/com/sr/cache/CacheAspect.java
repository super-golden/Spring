package com.sr.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Aspect
public class CacheAspect {


	@Around(value = "@annotation(guavaCache)")
	public void doCache(ProceedingJoinPoint joinPoint, GuavaCache guavaCache) throws Throwable {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();
		System.out.println("method:==>" + method + "	args:==>" + Arrays.asList(args));
		String reslut = parseExpression(guavaCache.spelKey(), method, args);
		System.out.println("guavaCache key:"+reslut);
		Object proceed = joinPoint.proceed();
		System.out.println(proceed);


	}


	/**
	 *
	 * @param expressionString
	 * @param method
	 * @param args
	 * @return
	 */
	private String parseExpression(String expressionString, Method method, Object[] args) {

		LocalVariableTableParameterNameDiscoverer nameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
		String[] parameterNames = nameDiscoverer.getParameterNames(method);
		//Spel 解析
		SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

		StandardEvaluationContext context = new StandardEvaluationContext();
		for (int i = 0; i < parameterNames.length; i++) {
			context.setVariable(parameterNames[i],args[i]);
		}
		String value = spelExpressionParser.parseExpression(expressionString).getValue(context, String.class);
		return value;
	}


}
