package com.pps.back.frame.pupansheng.core.transaction.processor;

import com.pps.back.frame.pupansheng.core.transaction.MethodInvocationHander;
import com.pps.back.frame.pupansheng.core.transaction.MutiDbTransactionUtil;
import com.pps.back.frame.pupansheng.core.transaction.annotion.MutilDbTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @discription;
 * @time 2020/11/5 15:06
 */

@Slf4j
public class MutilDbProcessor implements BeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        if(aClass.isAnnotationPresent(MutilDbTransactional.class)){
            log.info("{} 被MutilDbTransactional注解修饰  现在为它生成代理对象-------------",beanName);
            try {
                return   new MethodInvocationHander(bean,null,beanFactory).createProxy();
            } catch (Exception e) {
                e.printStackTrace();
                throw  new RuntimeException(bean+"：创建代理失败！！");
            }

        }else{
            Method[] declaredMethods = aClass.getDeclaredMethods();
            Map<String,MutilDbTransactional> methodMutilDbTransactionalMap=new HashMap<>();
            for(Method m:declaredMethods){
                if(m.isAnnotationPresent(MutilDbTransactional.class)) {
                    methodMutilDbTransactionalMap.put(MutiDbTransactionUtil.keyGenarate(m),m.getAnnotation(MutilDbTransactional.class));
                }
            }
            if(methodMutilDbTransactionalMap.keySet().size()>0){
                log.info("{} 类 有方法： 被MutilDbTransactional注解修饰  现在为它生成代理对象-------------",beanName);
                try {
                    return   new MethodInvocationHander(bean,methodMutilDbTransactionalMap,beanFactory).createProxy();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw  new RuntimeException("创建代理对象失败！");
                }
            }


        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

      return  bean;
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }


}
