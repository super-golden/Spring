package com.sr.ioc.ioc2.processores;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.util.StringValueResolver;

import java.util.HashSet;
import java.util.Set;

public class ObscenityRemoveingBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	private Set<String> obscenities;

	public ObscenityRemoveingBeanFactoryPostProcessor() {
		obscenities = new HashSet<>();
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		String[] beanNames = beanFactory.getBeanDefinitionNames();
		for (String beanName : beanNames) {
			BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
			StringValueResolver valueResolver = new StringValueResolver() {
				@Override
				public String resolveStringValue(String strVal) {
					if (isObscene(strVal)) {
						return "*******";
					}
					return strVal;
				}
			};

			BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);
			visitor.visitBeanDefinition(bd);
		}

	}

	public boolean isObscene(Object value) {
		String pot = value.toString().toUpperCase();
		return this.obscenities.contains(pot);
	}

	public void setObscenities(Set<String> obscenities) {
		this.obscenities.clear();
		for (String obscenity : obscenities) {
			this.obscenities.add(obscenity.toUpperCase());
		}
	}
}
