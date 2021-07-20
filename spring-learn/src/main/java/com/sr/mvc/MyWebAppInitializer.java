package com.sr.mvc;


import com.sr.mvc.config.RootConfig;
import com.sr.mvc.config.WebConfig;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    //根容器 父容器
    @Override
    protected Class<?>[] getRootConfigClasses() {
        logger.info("===========spring根容器加载成功============");
        return new Class[]{RootConfig.class};
    }

    //web容器 子容器
    @Override
    protected Class<?>[] getServletConfigClasses() {
        logger.info("===========springweb子容器加载成功============");
        return new Class[]{WebConfig.class};
    }

    //获取前端控制器映射信息 dispatcherServlet
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

    }
}
