package com.pps.back.frame.pupansheng.core.plug.insertplug;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author pps
 * @discription;
 * @time 2020/11/10 10:51
 */
@Configuration
public class InsertHelperPlugAutoConfigure {

    @Autowired
    List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public  void init(){

        sqlSessionFactoryList.forEach(sqlSessionFactory -> {

            sqlSessionFactory.getConfiguration().addInterceptor(new InsertHelperPlug());

        });


    }


}
