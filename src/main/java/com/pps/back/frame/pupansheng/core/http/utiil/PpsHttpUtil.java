package com.pps.back.frame.pupansheng.core.http.utiil;

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

       return  new PpsHttpUtilExcute(false,restTemplate);

    }

    /**
     * 创建异步的请求器
     * @return
     */
    public  static PpsHttpUtilExcute   createASyncClient(){

        return  new PpsHttpUtilExcute(true,restTemplate);

    }







}
