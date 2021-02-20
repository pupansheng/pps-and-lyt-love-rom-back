package com.pps.back.frame.pupansheng.core.http.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于用在htp请求的参数上 基本类型必须携带  其他类型的name 将不会起作用
 */
@Target({ElementType.PARAMETER,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpParam {

    String name() default "";

    /**
     * 参数类型  body 携带在请求体中 否则将会转变成url携带在url上面 get请求时 此注解无效 默认变成url
     * @return
     */
    String paramType() default "body";

}
