package com.pps.back.frame.pupansheng.core.plug.insertplug.annotion;

import java.lang.annotation.*;

@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoGenegrateId {

   String [] fieldNames() default {"id"};
   Class  [] fieldClass() default {String.class};

}
