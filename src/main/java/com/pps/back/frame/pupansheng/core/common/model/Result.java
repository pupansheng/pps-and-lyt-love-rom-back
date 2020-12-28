package com.pps.back.frame.pupansheng.core.common.model;

import com.pps.back.frame.pupansheng.core.authority.security.component.exception.GlobalCode;

/**
 * @author
 * @discription;
 * @time 2020/5/13 14:49
 */
public class Result {


    private Integer code;

    private String error;

    private  Object data;

    private  boolean status;



    public  static  Result ok(Object data){

       Result result=new Result();
       result.setCode(GlobalCode.Success.getTypeCode());
       result.setData(data);
       result.setStatus(true);

        return  result;
    }



    public  static  Result err(String erroInfo){


        Result result=new Result();
        result.setCode(GlobalCode.SerivceError.getTypeCode());
        result.setStatus(false);
        result.setError(erroInfo);
        return  result;


    }

    public  static  Result err(Integer code,String erroInfo){


        Result result=new Result();
        result.setCode(code);
        result.setStatus(false);
        result.setError(erroInfo);
        return  result;


    }


    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
