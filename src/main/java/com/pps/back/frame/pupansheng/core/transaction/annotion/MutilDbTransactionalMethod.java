package com.pps.back.frame.pupansheng.core.transaction.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MutilDbTransactionalMethod {
    String dataBase() default "dataBase";
}
