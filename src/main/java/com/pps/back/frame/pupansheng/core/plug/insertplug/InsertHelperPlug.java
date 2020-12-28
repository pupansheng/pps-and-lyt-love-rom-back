package com.pps.back.frame.pupansheng.core.plug.insertplug;


import com.pps.back.frame.pupansheng.core.plug.insertplug.annotion.AutoGenegrateId;
import com.pps.back.frame.pupansheng.core.plug.insertplug.strage.ObjectCreateFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author pps
 * @discription; 插入自动生成id插件
 * @time 2020/11/10 10:46
 */
  @Intercepts({@Signature(
            type=  Executor.class,
            method = "update",
            args = {MappedStatement.class,Object.class})})
    public class InsertHelperPlug implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        if (args.length == 2 && args[1] != null) {
            Object paramObject = args[1];
            Class<?> aClass = paramObject.getClass();
            AutoGenegrateId annotation = aClass.getAnnotation(AutoGenegrateId.class);
            if (annotation != null) {
                String[] strings = annotation.fieldNames();
                Class[] classes = annotation.fieldClass();

                for (int i = 0; i < strings.length; i++) {
                    String name = strings[i];
                    Field declaredField = aClass.getDeclaredField(name);
                    if (declaredField != null) {
                        declaredField.setAccessible(true);
                        if (declaredField.get(paramObject) == null) {
                            declaredField.set(paramObject, ObjectCreateFactory.getObjectCreate(classes[i]).product());
                            args[1] = paramObject;
                        }
                    } else {
                        throw new RuntimeException("注解[" + name + "] 的域在实体类中不存在");
                    }
                }

            }
        }
        Object proceed = invocation.proceed();
        return proceed;
    }

    @Override
    public Object plugin(Object o) {

        return Plugin.wrap(o, this);

    }

    @Override
    public void setProperties(Properties properties) {

    }

}

