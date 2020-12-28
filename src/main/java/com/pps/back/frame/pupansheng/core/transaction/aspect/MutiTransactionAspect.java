package com.pps.back.frame.pupansheng.core.transaction.aspect;

import com.pps.back.frame.pupansheng.core.transaction.annotion.MutilDbTransactionalMethod;
import com.pps.back.frame.pupansheng.core.transaction.MutiDbTransactionUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @discription;
 * @time 2020/11/5 15:10
 */
@Component
@Aspect
@Slf4j
public class MutiTransactionAspect {

    private static final ThreadLocal<Map<Connection,Boolean>>  conExeResult=ThreadLocal.withInitial(()->new HashMap<>());
    private static final ThreadLocal<List<DataSource>>  dataSourceUseList=ThreadLocal.withInitial(()->new ArrayList<>());


    @Autowired
    BeanFactory beanFactory;

    @Pointcut("@annotation(com.pps.back.frame.pupansheng.core.transaction.annotion.MutilDbTransactionalMethod)")
    public void transaction(){};
    @Around("transaction()")
    public Object round(ProceedingJoinPoint joinPoint) throws SQLException {

        MutiDbTransactionUtil.enterMutlTransaction();


        //获取方法参数值数组
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();
        MutilDbTransactionalMethod annotation = method.getAnnotation(MutilDbTransactionalMethod.class);
        String qualifier = annotation.dataBase();
        DataSource dataSource = BeanFactoryAnnotationUtils.qualifiedBeanOfType(beanFactory, DataSource.class, qualifier);
        if(dataSource==null){
            throw  new RuntimeException(qualifier+"数据源不存在容器中----------------");
        }
        Map<Connection, Boolean> connectionBooleanMap = conExeResult.get();
        Connection connection=null;
        Object resource = TransactionSynchronizationManager.getResource(dataSource);
        if(resource!=null){
            connection=((ConnectionHolder)resource).getConnection();
        }else {
            connection = dataSource.getConnection();
            ConnectionHolder connectionHolder=new ConnectionHolder(connection);
            TransactionSynchronizationManager.bindResource(dataSource,connectionHolder);
            dataSourceUseList.get().add(dataSource);
        }

        connection.setAutoCommit(false);

        Object result = null;
        Boolean error=false;
        try {
            result = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            error=true;
            throwable.printStackTrace();
        }finally {
            MutiDbTransactionUtil.releaseMutlTransaction();
        }
        connectionBooleanMap.put(connection,error);

        //事务发起方法结束
        if(MutiDbTransactionUtil.isFirstEnterMethod()){

            try {
                boolean errorHappen = connectionBooleanMap.values().stream().anyMatch(p -> p == true);
                if (errorHappen) {
                    log.info("发生错误  全部回滚------------------");
                    connectionBooleanMap.keySet().forEach(con -> {
                        try {
                            con.rollback();
                            log.info("数据库连接：{} 已回滚",con);
                        } catch (SQLException e) {
                            throw new RuntimeException("数据库连接回滚错误！！");
                        }
                    });
                    log.info("全部回滚结束------------------");
                } else {
                    log.info("没有发生错误  全部提交------------------");
                    connectionBooleanMap.keySet().forEach(con -> {
                        try {
                            con.commit();
                            log.info("数据库连接：{} 已提交",con);
                        } catch (SQLException e) {
                            throw new RuntimeException("数据库连接提交错误！！");
                        }
                    });
                    log.info("全部提交结束------------------");
                }
            }finally {
                log.info("归还数据库连接------------------");
                conExeResult.get().keySet().forEach(con->{
                    try {
                        con.close();
                    } catch (SQLException e) {
                        log.info("数据库连接：{} 关闭错误",con);
                    }
                });
                log.info("归还数据库连接结束------------------");
                conExeResult.remove();
                MutiDbTransactionUtil.reset();
                dataSourceUseList.get().forEach(d->{
                    TransactionSynchronizationManager.unbindResource(d);
                });
                dataSourceUseList.remove();
            }


        }


        return result;

    }
}
