package com.pps.back.frame.pupansheng.core.exceptionHander;

import com.pps.back.frame.pupansheng.core.authority.security.component.exception.AuthorizationException;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.ServiceException;
import com.pps.back.frame.pupansheng.core.common.model.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author
 * @discription;
 * @time 2020/12/24 17:01
 */
@ControllerAdvice
public class GlobalExceptionHander {

    @ExceptionHandler
    @ResponseBody
    public Result errorHandler(Exception ex) {
        int code;
        String message;
        if (ex instanceof AuthorizationException) {
            AuthorizationException temp = (AuthorizationException) ex;
            code = GlobalCode.AuthorizationError.getTypeCode();
            message = GlobalCode.AuthorizationError.getMsg();
        } else if (ex instanceof ServiceException) {
            code = GlobalCode.SerivceError.getTypeCode();
            message = ex.getMessage();
        } else {
            // 其他系统错误的日志无需返回客户端提示
            code = GlobalCode.SystemError.getTypeCode();
            message ="系统错误";
        }

        ex.printStackTrace();

        return Result.err(code,message);
    }


}
