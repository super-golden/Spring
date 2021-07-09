//package com.sr.extend;
//
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ExtendInstantiationAwareBeanPostProcessor  implements InstantiationAwareBeanPostProcessor {
//
//	/**
//
//	 *在bean被实例化之后，通过构造函数或工厂方法执行操作，
//
//	 *但在Spring属性填充（来自显式属性或自动连接）发生之前。
//
//	 *<p>这是对给定bean执行自定义字段注入的理想回调
//
//	 *例如，就在Spring的自动注入开始之前。
//
//	 *<p>默认实现返回{@code true}。
//
//	 *@param bean创建的bean实例，尚未设置属性
//
//	 *@param bean
//
//	 *@return{@code true}如果应该在bean上设置属性；{@code false}
//
//	 *是否应跳过属性填充。普通实现应该返回{@code true}。
//
//	 *返回{@code false}也会阻止任何后续的实例化warebeanpostprocessor的bean实例在这个实例上的调用。
//
//
//	 *@见实例化前的后处理
//
//	 */
//	@Override
//	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
//		return false;
//	}
//
////	@Override
////	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
////		return null;
////	}
//}
