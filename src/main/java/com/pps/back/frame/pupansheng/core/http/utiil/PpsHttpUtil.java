package com.pps.back.frame.pupansheng.core.http.utiil;

import com.pps.back.frame.pupansheng.custom.pachong.myrequest.Netty4ClientHttpRequest;
import com.pps.back.frame.pupansheng.custom.pachong.myrequest.Netty4ClientHttpRequestFactory;
import io.netty.handler.codec.http.HttpVersion;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;

/**
 * @author pps
 * @discription;
 * @time 2021/1/21 14:04
 */
public class PpsHttpUtil {

    private static RestTemplate restTemplate=new RestTemplate();

    /**
     * 创建同步的请求器
     * @return
     */
    public  static  PpsHttpUtilExcute   createSyncClient(){

      // restTemplate.setRequestFactory();
       return  new PpsHttpUtilExcute(false,restTemplate);

    }

    /**
     * 创建异步的请求器
     * @return
     */
    public  static PpsHttpUtilExcute   createASyncClient(){

        return  new PpsHttpUtilExcute(true,restTemplate);

    }


    /**
     * 创建同步的请求器
     * @return
     */
    public  static  PpsHttpUtilExcute   createCustomClient(HttpVersion httpVersion,boolean isAsync){

        // restTemplate.setRequestFactory();
        if(httpVersion==HttpVersion.HTTP_1_0) {
            restTemplate.setRequestFactory(new Netty4ClientHttpRequestFactory());
        }
        return  new PpsHttpUtilExcute(isAsync,restTemplate);

    }







}
