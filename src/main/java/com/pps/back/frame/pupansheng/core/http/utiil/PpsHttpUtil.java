package com.pps.back.frame.pupansheng.core.http.utiil;

import com.pps.back.frame.pupansheng.core.http.myrequest.Netty4ClientHttpRequestFactory;
import io.netty.handler.codec.http.HttpVersion;
import org.springframework.web.client.RestTemplate;

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
     * 创建自定义的请求器
     * @return
     */
    public  static  PpsHttpUtilExcute   createCustomClient(HttpVersion httpVersion, boolean isAsync){

        restTemplate.setRequestFactory(new Netty4ClientHttpRequestFactory(httpVersion));
        return  new PpsHttpUtilExcute(isAsync, restTemplate);

    }







}
