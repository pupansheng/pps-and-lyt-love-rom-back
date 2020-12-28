package com.pps.back.frame.pupansheng.core.plug.filterplug;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Properties;

/**
 * @author
 * @discription;
 * @time 2020/10/9 10:03
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
), @Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
)})
@Slf4j
public class DbFilterInterceptor implements Interceptor {
    private static final String FLAG="SELECT count(0)";
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
           Condition condition=null;
        if((condition= DbFilterHelper.getCondition())!=null){
            boolean isClear=true;
            try {
                Object[] args = invocation.getArgs();
                MappedStatement ms = (MappedStatement) args[0];
                Object parameter = args[1];
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler resultHandler = (ResultHandler) args[3];
                Executor executor = (Executor) invocation.getTarget();
                CacheKey cacheKey;
                BoundSql boundSql;
                if (args.length == 4) {
                    boundSql = ms.getBoundSql(parameter);
                    cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
                } else {
                    cacheKey = (CacheKey) args[4];
                    boundSql = (BoundSql) args[5];
                }
                String sql = boundSql.getSql();
                if(sql.startsWith(FLAG)){
                    isClear=false;
                }
                List<Object> objects = DbFilterExecutorUtil.filterQuery(condition, executor, ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
                return objects;
            }catch (Exception e){
                DbFilterHelper.clearCondition();
                throw new RuntimeException(e);
            }finally {
                if(isClear) {
                    DbFilterHelper.clearCondition();
                }
            }
        }else {
            return invocation.proceed();
        }




    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
      log.info("sql 过滤器插件启动成功");
    }
}
