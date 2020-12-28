package com.pps.back.frame.pupansheng.core.datasource;

import com.pps.back.frame.pupansheng.core.common.util.ValidateUtil;
import com.pps.back.frame.pupansheng.core.mymaperscan.MyClassPathMapperScanner;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * @discription;
 * @time 2020/9/11 21:50
 */
@Slf4j
public class MyBatisDataSourceProcessor implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    private final String DATASOURCE_DATA_LIST = "spring.dataSourseList";
    private final String DATASOURCE_PREFIX = "spring";
    private final String SPILIT = ",";
    private final String ADD_CHAR = ".";
    private Binder binder;

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        binder = Binder.get(this.getEnvironment());
        String dataSourceList = environment.getProperty(DATASOURCE_DATA_LIST);

        if (ValidateUtil.isNotEmpty(dataSourceList)) {
            log.info("数据源配置开始---------------------------------------------------------------------------------------");
            String[] dataSourses = dataSourceList.split(SPILIT);
            boolean isFirst=true;
            for (String dataSourseName : dataSourses) {

                BindResult<BaseSourceConfig> bind = binder.bind(DATASOURCE_PREFIX + ADD_CHAR + dataSourseName, BaseSourceConfig.class);
                BaseSourceConfig baseSourceConfig = bind.get();
                log.info("数据源：{} 自动配置 开始------------------------------------------------",dataSourseName);
                log.info("为数据源：{}配置数据源--------------------------------",dataSourseName);
                //配置数据源
                {

                    Map dataSourceProperties= (Map) this.binder.bind(DATASOURCE_PREFIX + ADD_CHAR + dataSourseName+".database", Bindable.of(Map.class)).get();
                    Class<?> aClass = null;
                    if(ValidateUtil.isEmpty(baseSourceConfig.getType())||(aClass= Class.forName(baseSourceConfig.getType()))==null){
                        log.warn("指定的数据源类型为空或类库不存在 ！！！应用采用默认的数据库源:{}数据源"," HikariDataSource.class");
                        aClass= HikariDataSource.class;
                    }
                    Map map = validateConfigMap(aClass, dataSourceProperties);
                    registerBean(beanDefinitionRegistry, dataSourseName,map, aClass,isFirst);
                }
                log.info("为数据源：{}配置数据源：{}  结束---------------------------",dataSourseName,dataSourseName);
                log.info("为数据源：{}配置事务管理器-----------------------------",dataSourseName);
                //配置事务管理器
                {
                    Class tr= DataSourceTransactionManager.class;
                    log.warn("---采用的事务处理管理器为：{}",tr.getName());
                    registerBean(beanDefinitionRegistry, dataSourseName + "TransactionManager", this.buildTransactionManager(DATASOURCE_PREFIX, dataSourseName,tr), tr,isFirst);
                }
                log.info("为数据源：{}配置事务管理器 ：{}  结束--------------------------",dataSourseName,dataSourseName + "TransactionManager");
                log.info("为数据源：{} 配置SqlSessionFactory工厂：{}--------------------------------",dataSourseName,dataSourseName + "SqlSessionFactory");
                //配置sessiong工厂
                {

                    registerBean(beanDefinitionRegistry, dataSourseName + "SqlSessionFactory", this.buildSqlSessionFactoryBean(binder, dataSourseName, baseSourceConfig), SqlSessionFactoryBean.class,isFirst);

                }
                log.info("为数据源：{} 配置SqlSessionFactory工厂：{}---------结束---------------------",dataSourseName,dataSourseName + "SqlSessionFactory");
                //注册mapper扫描
                log.info("为数据源：{} 配置mapper位置 >>>>>>>>--------------------------------",dataSourseName);
                {
                    registerMapper(beanDefinitionRegistry, baseSourceConfig, dataSourseName,dataSourseName + "SqlSessionFactory");
                }
                log.info("为数据源：{} 配置mapper位置 结束>>>>>>>>--------------------------------",dataSourseName);
                log.info("数据源：{} 自动配置 结束------------------------------------------------",dataSourseName);
                if(isFirst) {
                    isFirst = false;
                }
            }

        } else {
            log.warn("未检查到数据源名称 无法启动数据源自动配置");
        }

        log.info("数据源配置结束---------------------------------------------------------------------------------------");


    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    private void registerMapper(BeanDefinitionRegistry beanDefinitionRegistry, BaseSourceConfig baseSourceConfig, String dataSourseName,String sessionFactoryName) {

        String[] mappersList = baseSourceConfig.getMybatisConfig().getMapperLocation();
        if (ValidateUtil.isEmpty(mappersList)) {
            throw new RuntimeException("对于数据源：" + dataSourseName + ",指定的mapper包名  为空");
        }
        log.info("配置数据源：{}  mappper  位置-------------------------------------------------------", dataSourseName);
        for (String mapperPackage : mappersList) {
            log.info("为数据源：{}  配置dao接口 mapper 位置：{}", dataSourseName, mapperPackage);
          //  ClassPathMapperScanner scanner = new ClassPathMapperScanner(beanDefinitionRegistry);
            MyClassPathMapperScanner scanner=new MyClassPathMapperScanner(beanDefinitionRegistry);
            scanner.setSqlSessionFactoryBeanName(sessionFactoryName);
            scanner.registerFilters();
            scanner.doScan(mapperPackage);

        }
        log.info("配置数据源：{}  mappper  位置结束-----------------------------------------------------", dataSourseName);
    }

    private void registerBean(BeanDefinitionRegistry registry, String beanName, Map<String, Object> attributeMap, Class<?> clazz,Boolean primary) {
        BeanDefinitionBuilder dbBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        RootBeanDefinition dbBeanDefinition = (RootBeanDefinition) dbBuilder.getBeanDefinition();
        dbBeanDefinition.getPropertyValues().addPropertyValues(attributeMap);
        dbBeanDefinition.setPrimary(primary);
        registry.registerBeanDefinition(beanName, dbBeanDefinition);
    }

    protected Map<String, Object> buildTransactionManager(String prefix, String beanName,Class tr) {
        Map<String, Object> dstmMap = new HashMap(5);
        dstmMap.put("dataSource", new RuntimeBeanReference(beanName));
        Map<String, Object> map = this.extendAttributes(prefix + "." + beanName + ".transaction");
        if(map!=null) {
            dstmMap.putAll(validateConfigMap(tr,map));
        }
        return dstmMap;
    }

    protected Map<String, Object> extendAttributes(String path) {
        Binder binder = Binder.get(this.environment);

        try {
            return (Map) binder.bind(path, Bindable.of(HashMap.class)).get();
        } catch (Exception var4) {
            return null;
        }
    }

    private Map<String, Object> buildSqlSessionFactoryBean(Binder binder, String beanName, BaseSourceConfig baseSourceConfig) {
        Map<String, Object> sqlMap = new HashMap(5);

        class Util {

            protected String cutEndChar(List<String> list) {
                StringBuilder sb = new StringBuilder();
                list.stream().forEach((x) -> {
                    sb.append(x).append(",");
                });
                return sb.toString().substring(0, sb.length() - 1);
            }

            private DatabaseIdProvider buildDatabaseIdProvider() {
                DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
                Properties properties = new Properties();
                Arrays.stream(DriverType.values()).forEach((driverType) -> {
                    properties.setProperty(driverType.getPattern(), driverType.getType());
                });
                databaseIdProvider.setProperties(properties);
                return databaseIdProvider;
            }

            private TypeHandler[] buildTypeHandle() {
                TypeHandler[] typeHandlers = new TypeHandler[]{new LvarStringTypeHandler()};
                return typeHandlers;
            }

        }
        Util util = new Util();
        sqlMap.put("dataSource", new RuntimeBeanReference(beanName));
        List<String> typeAliasesPackageList =null;

        if (StringUtils.isEmpty(baseSourceConfig.getMybatisConfig().getTypeAliasesPackageList())) {
            log.warn("警告：数据源：{}-----------type-aliases-package属性为空",beanName);
        }else {
            typeAliasesPackageList= Arrays.asList(baseSourceConfig.getMybatisConfig().getTypeAliasesPackageList());
        }
        if (StringUtils.isEmpty(baseSourceConfig.getMybatisConfig().getMapperXmlLocation())) {
            throw new RuntimeException("mapper-xml-location属性为空");
        }

        List<String> mapperLocationsList = Arrays.asList(baseSourceConfig.getMybatisConfig().getMapperXmlLocation());

        if (!ValidateUtil.isEmpty(typeAliasesPackageList)) {
            sqlMap.put("typeAliasesPackage", util.cutEndChar(typeAliasesPackageList));
        }
        sqlMap.put("mapperLocations", mapperLocationsList);
        sqlMap.put("databaseIdProvider", util.buildDatabaseIdProvider());
        sqlMap.put("typeHandlers", util.buildTypeHandle());
        Map<String, Object> extendMap = this.extendAttributes(DATASOURCE_PREFIX + ADD_CHAR + beanName + ".sqlsessionfactory");
        if (extendMap != null) {
            sqlMap.putAll(extendMap);
        }
        return sqlMap;
    }

    protected Map<String, Object> validateConfigMap(Class obj, Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap(10);
        map.keySet().stream().forEach((key) -> {

            String methodName = "get";

            //驼峰转换
            if(key.contains("_")){
                key=  Arrays.stream(key.split("_")).map(s->s.substring(0, 1).toUpperCase().concat(s.substring(1))).collect(Collectors.joining());
            }
            key=key.substring(0, 1).toLowerCase().concat(key.substring(1));
            methodName = methodName + key.substring(0, 1).toUpperCase().concat(key.substring(1));
            try {
                obj.getMethod(methodName);
                newMap.put(key, map.get(key));
            } catch (Exception var7) {

                log.warn("{} 属性：  {}不存在  这个配置不合理",obj.getName(), key);
            }

        });
        return newMap;
    }

}





