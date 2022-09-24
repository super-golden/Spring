package com.sr.ioc.ioc4.testImport.factory;

import com.sr.ioc.ioc4.testImport.beans.Color;
import org.springframework.beans.factory.FactoryBean;

public class ColorFactoryBean implements FactoryBean<Color> {
	@Override
	public Color getObject() throws Exception {
		return new Color();
	}

	@Override
	public Class<?> getObjectType() {
		return Color.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
