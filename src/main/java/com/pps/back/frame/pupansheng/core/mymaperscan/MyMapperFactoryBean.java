package com.pps.back.frame.pupansheng.core.mymaperscan;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @discription;
 * @time 2020/10/19 11:44
 */
public class MyMapperFactoryBean<T> extends MapperFactoryBean {
   private ExecutorType executorType;
   private Map<String,Field> cache=new HashMap<>();
    public MyMapperFactoryBean(Class mapperInterface) {
        super(mapperInterface);
    }

    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        SqlSessionTemplate sqlSessionTemplate=null;
        if(executorType==null){
            sqlSessionTemplate=new SqlSessionTemplate(sqlSessionFactory);
        }else {
            sqlSessionTemplate=new SqlSessionTemplate(sqlSessionFactory,executorType);
        }
        Field sqlSession=cache.get("sqlSession");
        if(sqlSession==null) {
            Class<SqlSessionDaoSupport> sqlSessionDaoSupportClass = SqlSessionDaoSupport.class;
            try {
                sqlSession = sqlSessionDaoSupportClass.getDeclaredField("sqlSession");
                sqlSession.setAccessible(true);
                cache.put("sqlSession",sqlSession);
            } catch (Exception e) {
                throw  new RuntimeException(e);
            }
        }
        try {
            sqlSession.set(this, sqlSessionTemplate);
        } catch (IllegalAccessException e) {
            throw  new RuntimeException(e);
        }
    }

    public ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }
}
