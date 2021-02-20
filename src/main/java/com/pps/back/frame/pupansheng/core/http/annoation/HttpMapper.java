package com.pps.back.frame.pupansheng.core.http.annoation;

import com.pps.back.frame.pupansheng.core.http.model.PostType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpMapper {

    /*
      http接口请求baseUrl
     */
     String baseUrl();


    /**
     * 请求方法
     * @return
     */
     String method() default "post";

    /**
     * 请求类型  post方法有效
     * @return
     */
     PostType type() default PostType.APPLICATION_XFOM;

    /**
     * 请求头
     * @return
     */
     HttpHeader[] headers() default {};

}
