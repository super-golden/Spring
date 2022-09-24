package com.sr.cache;

import com.google.common.cache.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class CacheAspect {


	@Around(value = "@annotation(guavaCache)")
	public Object doCache(ProceedingJoinPoint joinPoint, GuavaCache guavaCache) throws Throwable {

		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();
		System.out.println("method:==>" + method + "	args:==>" + Arrays.asList(args));
		String key = parseExpression(guavaCache.spelKey(), method, args);
		System.out.println("guavaCache key:" + key);
		LoadingCache<String, byte[]> loadingCache = GuavaCacheProcess.getLoadingCache(joinPoint, guavaCache);
		byte[] bytes = loadingCache.get(key);
		return SerializationUtils.deserialize(bytes);


//		Object proceed = joinPoint.proceed();
//		System.out.println(proceed);


	}


	/**
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
			context.setVariable(parameterNames[i], args[i]);
		}
		String value = spelExpressionParser.parseExpression(expressionString).getValue(context, String.class);
		return value;
	}


	public static class GuavaCacheProcess {

		private static Map<String, LoadingCache<String, byte[]>> cacheLoaders = new HashMap<>();

		public void process() {

		}


		public static LoadingCache<String, byte[]> getLoadingCache(ProceedingJoinPoint joinPoint, GuavaCache guavaCache) {
			LoadingCache<String, byte[]> cacheLoad = cacheLoaders.get(guavaCache.group());
			if (cacheLoad == null) {
				MethodSignature signature = (MethodSignature) joinPoint.getSignature();
				CommonCacheLoader commonCacheLoade = new CommonCacheLoader(guavaCache.group(),
						joinPoint.getTarget(),
						signature.getMethod(),
						joinPoint.getArgs());
				CacheBuilder builder = CacheBuilder.newBuilder().expireAfterWrite(guavaCache.expire(), TimeUnit.SECONDS);
				builder.removalListener(commonCacheLoade);
				cacheLoad = builder.build(commonCacheLoade);
				cacheLoaders.put(guavaCache.group(), cacheLoad);
			}
			return cacheLoad;

		}


		public static class CommonCacheLoader extends CacheLoader<String, byte[]> implements RemovalListener<String, byte[]> {

			private String group;

			/**
			 * 代理目标类
			 */
			private Object target;

			/**
			 * 代理目标方法
			 */
			private Method method;

			/**
			 * 参数
			 */
			private Object[] args;

			public CommonCacheLoader(String group, Object target, Method method, Object[] args) {
				this.group = group;
				this.target = target;
				this.method = method;
				this.args = args;
			}

			@Override
			public byte[] load(String key) throws Exception {
				Object invoke = method.invoke(target, args);
				//这里考虑序列化
				return SerializationUtils.serialize(invoke);
			}

			@Override
			public void onRemoval(RemovalNotification<String, byte[]> notification) {
				System.out.println(notification.getKey() + "remove ---------------cause:" + notification.getCause());
			}

		}
	}


}
