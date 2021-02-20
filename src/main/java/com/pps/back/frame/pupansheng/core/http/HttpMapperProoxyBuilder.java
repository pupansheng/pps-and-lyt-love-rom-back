package com.pps.back.frame.pupansheng.core.http;

import com.pps.back.frame.pupansheng.core.http.annoation.EnableHttpMapper;
import com.pps.back.frame.pupansheng.core.http.annoation.HttpMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author
 * @discription;
 * @time 2021/1/8 16:51
 */

public class HttpMapperProoxyBuilder implements ImportBeanDefinitionRegistrar,BeanFactoryAware {

    private BeanFactory beanFactory;
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        HttpMapperScanner httpMapperScanner=new HttpMapperScanner(registry);
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnableHttpMapper.class.getName());
        Object basePackage = annotationAttributes.get("basePackage");
        httpMapperScanner.setBeanFactory(beanFactory);
        httpMapperScanner.doScan((String [])basePackage);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory=beanFactory;
    }
}
