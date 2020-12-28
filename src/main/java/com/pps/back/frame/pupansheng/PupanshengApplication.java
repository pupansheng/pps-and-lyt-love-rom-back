package com.pps.back.frame.pupansheng;

import com.pps.back.frame.pupansheng.core.datasource.MyBatisDataSourceProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author
 * @discription;
 * @time 2020/12/23 17:30
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
@Import(MyBatisDataSourceProcessor.class)
@EnableTransactionManagement

//@EnableMutilDbTranactionManagement
public class PupanshengApplication {

    public static void main(String[] args) {
        SpringApplication.run(PupanshengApplication.class, args);
    }

}
