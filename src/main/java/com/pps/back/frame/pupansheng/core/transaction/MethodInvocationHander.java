package com.pps.back.frame.pupansheng.core.transaction;

import com.pps.back.frame.pupansheng.core.transaction.annotion.MutilDbTransactional;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author
 * @discription;
 * @time 2020/11/5 13:51
 */
public class MethodInvocationHander implements InvocationHandler {

    private Object target;
    private Class [] interfaceClasses;
    private ClassLoader classLoader;
    private MutilDbTransactional mutilDbTransactional;
    private BeanFactory beanFactory;
    private Map<String,MutilDbTransactional> methodMutilDbTransactionalMap;
    public MethodInvocationHander(Object targetO, Map<String,MutilDbTransactional> methodMutilDbTransactionalMap,BeanFactory beanFactory) throws IllegalAccessException, InstantiationException {
        Class<?> targetClass = targetO.getClass();
        Class[] interfaces = targetClass.getInterfaces();
        interfaceClasses=interfaces;
        mutilDbTransactional=targetClass.getDeclaredAnnotation(MutilDbTransactional.class);
        target= targetO;
        classLoader=targetClass.getClassLoader();
        this.beanFactory=beanFactory;
        this.methodMutilDbTransactionalMap=methodMutilDbTransactionalMap;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Invocation invocation=new Invocation(target,mutilDbTransactional,methodMutilDbTransactionalMap,method,args);
        MutiDbTransactionInterceptor mutiDbTransactionInterceptor = new MutiDbTransactionInterceptor(invocation,beanFactory);
        return mutiDbTransactionInterceptor.interceptor();

    }

    public Object createProxy(){

      return  Proxy.newProxyInstance(classLoader,interfaceClasses,this);

    }




}
