package com.pps.back.frame.pupansheng.core.transaction.annotion;

import com.pps.back.frame.pupansheng.core.transaction.processor.MutilDbProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MutilDbProcessor.class)
public @interface EnableMutilDbTranactionManagement {
}
