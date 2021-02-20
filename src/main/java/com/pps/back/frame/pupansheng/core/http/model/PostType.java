package com.pps.back.frame.pupansheng.core.http.model;

/**
 * @author
 * @discription;
 * @time 2021/1/11 12:32
 */
public enum  PostType {
    APPLICATION_JSON("application/json"),
    APPLICATION_XFOM("application/x-www-form-urlencoded");
    private String msg;
    PostType(String msg){
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
