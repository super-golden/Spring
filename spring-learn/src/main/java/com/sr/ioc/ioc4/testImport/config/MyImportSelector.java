package com.sr.ioc.ioc4.testImport.config;

import com.sr.ioc.ioc4.testImport.beans.Pig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportSelector implements ImportSelector {
	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[]{Pig.class.getName()};
	}
}
