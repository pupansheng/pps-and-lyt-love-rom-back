package com.pps.back.frame.pupansheng.core.http;

import com.pps.back.frame.pupansheng.core.http.annoation.HttpMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * @author
 * @discription;
 * @time 2021/1/8 16:51
 */

public class HttpMapperScanner extends ClassPathBeanDefinitionScanner  {

    private BeanFactory beanFactory;
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
       return  beanDefinition.getMetadata().isInterface();
    }

    @Override
    protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
        boolean b = metadataReader.getAnnotationMetadata().hasAnnotation(HttpMapper.class.getName());
        return b&&metadataReader.getClassMetadata().isInterface();
    }

    public HttpMapperScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public HttpMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    public HttpMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment) {
        super(registry, useDefaultFilters, environment);
    }

    public HttpMapperScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters, Environment environment, ResourceLoader resourceLoader) {
        super(registry, useDefaultFilters, environment, resourceLoader);
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            this.logger.warn("No http mapper was found in '" + Arrays.toString(basePackages) + "' package. Please check your configuration.");
        } else {
            this.processBeanDefinitions(beanDefinitions);
        }
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {


        for(BeanDefinitionHolder beanDefinitionHolder:beanDefinitions){

            BeanDefinitionHolder holder = (BeanDefinitionHolder)beanDefinitionHolder;
            GenericBeanDefinition definition = (GenericBeanDefinition)holder.getBeanDefinition();
            String interfaceName=definition.getBeanClassName();
            definition.getConstructorArgumentValues().addGenericArgumentValue(interfaceName);
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanFactory);
            definition.setBeanClass(HttpMapperBean.class);
        }


                 

    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }
}
