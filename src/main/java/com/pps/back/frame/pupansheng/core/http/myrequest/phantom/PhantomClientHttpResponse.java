/*
 * Copyright (c) ACCA Corp.
 * All Rights Reserved.
 */
package com.pps.back.frame.pupansheng.core.http.myrequest.phantom;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @author Pu PanSheng, 2021/4/4
 * @version OPRA v1.0
 */
public class PhantomClientHttpResponse implements ClientHttpResponse {


    private PhantomJSDriver phantomJSDriver;

    public PhantomClientHttpResponse(PhantomJSDriver driver) {
        this.phantomJSDriver=driver;
    }

    @Override
    public HttpStatus getStatusCode() throws IOException {
        return  HttpStatus.OK;
    }

    @Override
    public int getRawStatusCode() throws IOException {
        return 200;
    }

    @Override
    public String getStatusText() throws IOException {
        return "ok";
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getBody() throws IOException {
        String pageSource = phantomJSDriver.getPageSource();
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(pageSource.getBytes());
        return byteArrayInputStream;
    }

    public String getHtml(){
        return  phantomJSDriver.getPageSource();
    }

    public  PhantomJSDriver getDriver(){
        return  phantomJSDriver;
    }

    @Override
    public HttpHeaders getHeaders() {
        return null;
    }
}
