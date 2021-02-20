package com.pps.back.frame.pupansheng.core.http.utiil;

import com.github.pagehelper.PageException;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Method;

/**
 * @author
 * @discription;
 * @time 2021/1/9 20:40
 */
public class ReflectUtil {

    public static Method method;

    public static MetaObject forObject(Object object) {
        try {
            return (MetaObject)method.invoke((Object)null, object);
        } catch (Exception var2) {
            throw new PageException(var2);
        }
    }

    static {
        try {
            Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.SystemMetaObject");
            method = metaClass.getDeclaredMethod("forObject", Object.class);
        } catch (Exception var3) {
            try {
                Class<?> metaClass = Class.forName("org.apache.ibatis.reflection.MetaObject");
                method = metaClass.getDeclaredMethod("forObject", Object.class);
            } catch (Exception var2) {
                throw new RuntimeException(var2);
            }
        }

    }


}
