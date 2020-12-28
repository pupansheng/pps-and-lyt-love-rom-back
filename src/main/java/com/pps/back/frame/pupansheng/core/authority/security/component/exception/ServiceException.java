package com.pps.back.frame.pupansheng.core.authority.security.component.exception;

/**
 * @Classname UnknowException
 * @Description
 * @@Author Pupansheng
 * @Date 2019/12/21 15:17
 * @Vestion 1.0
 **/
public class ServiceException extends  RuntimeException {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }
}
