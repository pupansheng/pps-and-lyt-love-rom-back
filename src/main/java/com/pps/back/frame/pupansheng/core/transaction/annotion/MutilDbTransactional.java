package com.pps.back.frame.pupansheng.core.transaction.annotion;

import com.pps.back.frame.pupansheng.core.transaction.isolevel.IsolationLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface MutilDbTransactional {
    String dataBase() default "dataSource";
    String []  ignoreMethod() default {"equals","hashCode","toString"};
    boolean readOnly() default false;
    boolean autoCommit() default  false;
    IsolationLevel isolationLevel() default IsolationLevel.TRANSACTION_DEFAULT;
}
