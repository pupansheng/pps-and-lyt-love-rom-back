/*
 * Copyright (c) ACCA Corp.
 * All Rights Reserved.
 */
package com.pps.back.frame.pupansheng.core.http.myrequest.phantom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;


/**
 * @author Pu PanSheng, 2021/4/4
 * @version OPRA v1.0
 */
public class PhantomClientHttpRequest implements ClientHttpRequest {

    private URI uri;
    private HttpMethod httpMethod;
    private Consumer<DesiredCapabilities> config;
    private String path;
    private PhantomJSDriver driver;
    private HttpHeaders httpHeaders;
    private OutputStream outputStream;
    public PhantomClientHttpRequest(URI uri, HttpMethod httpMethod, Consumer<DesiredCapabilities> config,String path) {
        this.uri=uri;
        this.httpMethod=httpMethod;
        this.config=config;
        this.path=path;
        httpHeaders=new HttpHeaders();
        //设置必要参数
        outputStream=new ByteArrayOutputStream();
        DesiredCapabilities dcaps = new DesiredCapabilities();
        config.accept(dcaps);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,path);
        //创建无界面浏览器对象
        driver = new PhantomJSDriver(dcaps);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @Override
    public ClientHttpResponse execute() throws IOException {

        driver.get(uri.toString());
        PhantomClientHttpResponse phantomClientHttpResponse=new PhantomClientHttpResponse(driver);
        return phantomClientHttpResponse;
    }

    @Override
    public OutputStream getBody() throws IOException {
        return outputStream;
    }

    @Override
    public String getMethodValue() {
        return httpMethod.name();
    }

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public HttpHeaders getHeaders() {
        return  httpHeaders;
    }
}
