package com.pps.back.frame.pupansheng.core.mymaperscan.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ExecutorType {
    org.apache.ibatis.session.ExecutorType value() default org.apache.ibatis.session.ExecutorType.SIMPLE;
}
