/*
 * Copyright (c) ACCA Corp.
 * All Rights Reserved.
 */
package com.pps.back.frame.pupansheng.core.http.utiil;

/**
 * @author Pu PanSheng, 2021/4/4
 * @version OPRA v1.0
 */
public class PpsHttpUtilExcute2_0<T> {

    /**
     * 返回类型
     */
    private Class<T> responseType;
    /**
     * 是否异步
     */
    private boolean isAsync;



    /**
     * 构造http请求执行器
     */
    public PpsHttpUtilExcute2_0(Class<T> responseType,boolean isAsync){

        this.responseType=responseType;
        this.isAsync=isAsync;


    }




}
