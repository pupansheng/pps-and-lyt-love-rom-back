package com.pps.back.frame.pupansheng.core.mvc.annotion;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CanOtherUrlRequest {

    String [] value();

}
