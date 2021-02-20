package com.pps.back.frame.pupansheng.core.http.annoation;

import com.pps.back.frame.pupansheng.core.http.model.PostType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMethod {


    String method() ;


    HttpHeader[] headers() default {};


    String url() default "";

    /**
     * 请求类型  post方法有效
     * @return
     */
    PostType type() default PostType.APPLICATION_XFOM;

    /**
     * 是否覆盖类的注解相关参数 比如header
     */
    boolean  overParam() default false;
}
