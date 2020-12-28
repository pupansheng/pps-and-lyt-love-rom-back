package com.pps.back.frame.pupansheng.core.authority.security.uitl;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author
 * @discription;
 * @time 2020/6/22 16:33
 */



public class MyValidateUtil {


    public static   void validate(Object object,String ... fieldNames)  {
        Class<?> aClass = object.getClass();
        for(String fieldName:fieldNames ) {
            Field declaredField = null;
            try {
                declaredField = aClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                Class<?> superclass = aClass.getSuperclass();
                while (superclass != null) {
                    try {
                        declaredField = superclass.getDeclaredField(fieldName);
                        break;
                    } catch (NoSuchFieldException ex) {
                        superclass = superclass.getSuperclass();
                    }
                }
            }
            if (declaredField != null) {
                declaredField.setAccessible(true);
                Object o = null;
                try {
                    o = declaredField.get(object);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (Objects.isNull(o)) {
                    throw new RuntimeException(fieldName + ":不能为空");
                }
            }

        }
    }



}
