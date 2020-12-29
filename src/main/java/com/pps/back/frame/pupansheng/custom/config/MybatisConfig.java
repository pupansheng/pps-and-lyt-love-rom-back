package com.pps.back.frame.pupansheng.custom.config;

import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * @author pps
 * @discription;  //添加驼峰命名规则  配置文件方式不能正常使用
 * @time 2020/9/4 10:51
 */
@Configuration
public class MybatisConfig implements BeanDefinitionRegistryPostProcessor {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        Object ta404dsSqlSessionFactoryBean = configurableListableBeanFactory.getBean("data-source1SqlSessionFactory");
        DefaultSqlSessionFactory defaultSqlSessionFactory=(DefaultSqlSessionFactory)ta404dsSqlSessionFactoryBean;
        org.apache.ibatis.session.Configuration configuration = defaultSqlSessionFactory.getConfiguration();
        configuration.setMapUnderscoreToCamelCase(true);


    }
}
