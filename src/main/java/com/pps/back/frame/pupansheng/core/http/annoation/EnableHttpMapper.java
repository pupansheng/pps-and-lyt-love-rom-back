package com.pps.back.frame.pupansheng.core.http.annoation;

import com.pps.back.frame.pupansheng.core.http.HttpMapperProoxyBuilder;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HttpMapperProoxyBuilder.class)
public @interface EnableHttpMapper {
   String[] basePackage();
}
