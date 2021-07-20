//package com.sr.mvc.config;
//
//
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//@EnableWebMvc
//@ComponentScan(value = {"com.sr.mvc"}, includeFilters = {
//		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class})
//}, useDefaultFilters = false)
//@EnableAspectJAutoProxy
//public class WebConfig implements WebMvcConfigurer {
//
//	protected final Log logger = LogFactory.getLog(getClass());
//
//	/**
//	 * 配置springmvc视图
//	 *
//	 * @return
//	 */
//	@Override
//	public void configureViewResolvers(ViewResolverRegistry registry) {
//		logger.info("============jsp解析器开始加载===============");
//		System.out.println();
////        registry.jsp();
//
//		registry.jsp("/WEB-INF/views/", ".jsp");
//
//		logger.info("============jsp解析器开始成功，开始渲染把===============");
//	}
//
////    @Bean
////    public LogAopConfig logAopConfig() {
////        return new LogAopConfig();
////    }
//
//}
