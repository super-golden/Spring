package com.sr.cache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GuavaCache {

	/**
	 * 缓存分组
	 * @return
	 */
	String group() default "";

	/**
	 * 缓存key
	 * @return
	 */
	String spelKey();

	String value() default "";
}
