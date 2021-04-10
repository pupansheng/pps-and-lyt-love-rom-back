/*
 * Copyright (c) ACCA Corp.
 * All Rights Reserved.
 */
package com.pps.back.frame.pupansheng.core.http.myrequest.phantom;

import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

/**
 * @author Pu PanSheng, 2021/4/4
 * @version OPRA v1.0
 */
public class PhantomRequestFactory implements ClientHttpRequestFactory {

    private   String path="/usr/bin/phantomjs";//"D:\\dev_softwares\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe";

    Consumer<DesiredCapabilities> defaultConfig;
    public PhantomRequestFactory() {
        defaultConfig=(dcaps)->{
            //ssl证书支持
            dcaps.setCapability("acceptSslCerts", true);
            //截屏支持
            dcaps.setCapability("takesScreenshot", true);
            //css搜索支持
            dcaps.setCapability("cssSelectorsEnabled", true);
            //js支持
            dcaps.setJavascriptEnabled(true);

        };
    }
    public PhantomRequestFactory(Consumer<DesiredCapabilities> defaultConfig) {
       this.defaultConfig=defaultConfig;
    }

    public  void setPath(String path) {
        this.path = path;
    }

    @Override
    public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
        return new PhantomClientHttpRequest(uri,httpMethod,defaultConfig,path);
    }
}
