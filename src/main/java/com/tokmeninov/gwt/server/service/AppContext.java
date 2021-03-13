package com.tokmeninov.gwt.server.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;

public class AppContext implements ApplicationContextAware {

    private static ApplicationContext appContext;

    public AppContext() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> thisClass){
        return appContext.getBean(name,thisClass);
    }

    public static Object getBean(String name){
        return appContext.getBean(name);
    }

    public static <T> T getBean(Class<T> thisClass){
        return appContext.getBean(thisClass);
    }

}
