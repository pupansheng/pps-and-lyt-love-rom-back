package com.pps.back.frame.pupansheng.core.authority.security.component.exception;

public enum GlobalCode {

    Success(200,"成功"),
    AuthorizationError(1000,"权限错误"),
    SerivceError(10001,"业务错误"),
    SystemError(10002,"系统未知错误")
    ;
    private Integer typeCode;
    private String msg;

    GlobalCode(Integer typeCode, String msg) {
        this.typeCode = typeCode;
        this.msg = msg;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public String getMsg() {
        return msg;
    }
}
