package com.sr.testImport;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {MyBeanDefinitionRegister.class})
public class MyConfig {
}
