package com.pps.back.frame.pupansheng.core.http;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author
 * @discription;
 * @time 2021/1/9 17:17
 */
public class HttpMapperBean implements FactoryBean {

    private Class mapper;
    private BeanFactory beanFactory;
    public HttpMapperBean(String mapperName, BeanFactory beanFactory) {
        try {
            this.mapper = Class.forName(mapperName);
            this.beanFactory=beanFactory;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getObject() throws Exception {


        InvocationHandler invocationHandler = HttpInvocationHandler.crateProxy(mapper, beanFactory);
        Object o = Proxy.newProxyInstance(mapper.getClassLoader(), new Class[]{mapper}, invocationHandler);
        return o;
    }

    @Override
    public Class<?> getObjectType() {
        return mapper;
    }
}
