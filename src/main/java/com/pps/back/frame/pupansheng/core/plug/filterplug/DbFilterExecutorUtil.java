package com.pps.back.frame.pupansheng.core.plug.filterplug;

import com.github.pagehelper.PageException;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @discription;
 * @time 2020/10/9 10:47
 */
public class DbFilterExecutorUtil {
    private static Field additionalParametersField;
    public static MappedStatement getExistedMappedStatement(Configuration configuration, String msId) {
        MappedStatement mappedStatement = null;

        try {
            mappedStatement = configuration.getMappedStatement(msId, false);
        } catch (Throwable var4) {
        }

        return mappedStatement;
    }

    public static <E> List<E> filterQuery(Condition condition, Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql, CacheKey cacheKey) throws SQLException {
            String filterSqlStr=condition.filterSql(boundSql,parameter);
            parameter=condition.processParameterObject(ms, parameter, boundSql, cacheKey);
            BoundSql filterSql = new BoundSql(ms.getConfiguration(), filterSqlStr, boundSql.getParameterMappings(), parameter);
            Map<String, Object> additionalParameters = getAdditionalParameter(boundSql);
            Iterator var12 = additionalParameters.keySet().iterator();

            while(var12.hasNext()) {
                String key = (String)var12.next();
                filterSql.setAdditionalParameter(key, additionalParameters.get(key));
            }

            return executor.query(ms, parameter, RowBounds.DEFAULT, resultHandler, cacheKey, filterSql);

    }

    public static Map<String, Object> getAdditionalParameter(BoundSql boundSql) {
        try {
            return (Map)additionalParametersField.get(boundSql);
        } catch (IllegalAccessException var2) {
            throw new PageException("获取 BoundSql 属性值 additionalParameters 失败: " + var2, var2);
        }
    }

    static {
        try {
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException var1) {
            throw new PageException("获取 BoundSql 属性 additionalParameters 失败: " + var1, var1);
        }
    }
}
