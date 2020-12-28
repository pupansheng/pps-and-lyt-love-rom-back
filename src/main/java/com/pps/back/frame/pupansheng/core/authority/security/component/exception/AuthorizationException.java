package com.pps.back.frame.pupansheng.core.authority.security.component.exception;

/**
 * @Classname NoAuthorizationException
 * @Description
 * @@Author Pupansheng
 * @Date 2019/12/21 15:15
 * @Vestion 1.0
 **/
public class AuthorizationException extends   RuntimeException {

    public  AuthorizationException(){

        super();
    }

    public  AuthorizationException(String message){
        super(message);
    }

}
