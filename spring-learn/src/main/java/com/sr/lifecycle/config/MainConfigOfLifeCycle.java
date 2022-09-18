package com.sr.lifecycle.config;

import com.sr.lifecycle.beans.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * bean 的生命周期
 * bean创建---初始化---销毁的过程
 * 容器管理bean的生命周期：
 *   1、构造（对象创建）
 *
 *   2、初始化：
 *        对象创建完成，并赋值好，调用初始化方法。
 *
 *
 *  销毁
 *     容器关闭，调用销毁方法
 */
@ComponentScan(basePackages = "com.sr.lifecycle")
public class MainConfigOfLifeCycle {

	@Bean(initMethod = "init",destroyMethod = "destroy")
	public Car car() {
		return new Car();
	}
}
