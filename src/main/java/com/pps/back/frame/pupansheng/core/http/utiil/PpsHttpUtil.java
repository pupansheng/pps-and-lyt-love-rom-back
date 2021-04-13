package com.pps.back.frame.pupansheng.core.http.utiil;

import com.pps.back.frame.pupansheng.core.http.myrequest.netty.Netty4ClientHttpRequestFactory;
import com.pps.back.frame.pupansheng.core.http.myrequest.phantom.PhantomClientHttpResponse;
import com.pps.back.frame.pupansheng.core.http.myrequest.phantom.PhantomRequestFactory;
import com.pps.back.frame.pupansheng.core.http.strategy.DefaultContentTypeTextHtmlOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import io.netty.handler.codec.http.HttpVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * @author pps
 * @discription;
 * @time 2021/1/21 14:04
 */
@Slf4j
public class PpsHttpUtil {

    private static final int BUFF_SIZE=10;//普通网络请求
    private static final int BUFF_SIZE_NETTY=5;//可自定义网络协议版本的网络请求
    private static final int BUFF_SIZE_PHANTOMJS=5;//phantomjs 的网络请求
    private static RestTemplate restTemplates []=new RestTemplate[BUFF_SIZE];
    private static RestTemplate restTemplatesNetty []=new RestTemplate[BUFF_SIZE_NETTY];
    private static RestTemplate restTemplatesPhantomjs []=new RestTemplate[BUFF_SIZE_PHANTOMJS];
    private static Semaphore semaphore=new Semaphore(BUFF_SIZE);
    private static Semaphore semaphoreNetty=new Semaphore(BUFF_SIZE_NETTY);
    private static Semaphore semaphorePhantomjs=new Semaphore(BUFF_SIZE_PHANTOMJS);
    
    private static AtomicBoolean useD []=new AtomicBoolean[BUFF_SIZE];
    private static AtomicBoolean useDNetty []=new AtomicBoolean[BUFF_SIZE_NETTY];
    private static AtomicBoolean useDPhantomjs []=new AtomicBoolean[BUFF_SIZE_PHANTOMJS];
    static {
        for (int i = 0; i <restTemplates.length ; i++) {
            restTemplates[i]=new RestTemplate();
            useD[i]=new AtomicBoolean(false);
        }
        for (int i = 0; i <restTemplatesNetty.length ; i++) {
            restTemplatesNetty[i]=new RestTemplate();
            restTemplatesNetty[i].setRequestFactory(new Netty4ClientHttpRequestFactory());
            useDNetty[i]=new AtomicBoolean(false);
        }
        for (int i = 0; i <restTemplatesPhantomjs.length ; i++) {
            restTemplatesPhantomjs[i]=new RestTemplate();
            restTemplatesPhantomjs[i].setRequestFactory(new PhantomRequestFactory());
            useDPhantomjs[i]=new AtomicBoolean(false);
        }
    }


    public static RestTemplate getRestClient(){

        try {
            log.info("线程{}：尝试获取restClient："+semaphore.availablePermits(),Thread.currentThread().getId());
            semaphore.acquire();
            log.info("线程{}：获取成功：还有资源可用 尝试拿到未用的客户端",Thread.currentThread().getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i <BUFF_SIZE ; i++) {
            if(!useD[i].get()){
                if(useD[i].compareAndSet(false,true)) {
                    return restTemplates[i];
                }
            }
        }
        log.info("线程{}：尝试拿到未用的客户端 失败(当前的可用客户端已经被别的线程所用)：直接创造一个",Thread.currentThread().getId());
        return  new RestTemplate();
    }

    public static void returnClient(RestTemplate restTemplate){
        ClientHttpRequestFactory requestFactory = restTemplate.getRequestFactory();
        for (int i = 0; i <BUFF_SIZE ; i++) {
           if(restTemplate==restTemplates[i]){
               useD[i].compareAndSet(true,false);
               semaphore.release();
           }
        }

    }


    public static String autoTransfor2String(ClientHttpResponse clientHttpResponse){

        if(clientHttpResponse instanceof PhantomClientHttpResponse){
           PhantomClientHttpResponse phantomClientHttpResponse= ((PhantomClientHttpResponse) clientHttpResponse);
            String html = phantomClientHttpResponse.getHtml();
            phantomClientHttpResponse.getDriver().close();
            phantomClientHttpResponse.getDriver().quit();
            log.info("phantomjs 已经退出");
            return html;
        }

        MediaType contentType = clientHttpResponse.getHeaders().getContentType();
        HttpRequstOperation operation = HttpStrategyFactory
                .getOperation(contentType.toString());
        if(operation==null){
            operation=new DefaultContentTypeTextHtmlOperation();
        }
        Object o = operation.extractData(clientHttpResponse, String.class);
        String html = (String) o;

        return  html;

    }

    /**
     * 创建同步的请求器
     * @return
     */
    public  static  PpsHttpUtilExcute   createSyncClient(){
        RestTemplate restClient = getRestClient();
        restClient.setRequestFactory(new SimpleClientHttpRequestFactory());
        return  new PpsHttpUtilExcute(false,restClient);
    }

    /**
     * 创建异步的请求器
     * @return
     */
    public  static PpsHttpUtilExcute   createASyncClient(){
        RestTemplate restClient = getRestClient();
        restClient.setRequestFactory(new SimpleClientHttpRequestFactory());
        return  new PpsHttpUtilExcute(true,restClient);
    }


    /**
     * 创建http协议自定义的请求器
     * @return
     */
    public  static  PpsHttpUtilExcute   createCustomClient(HttpVersion httpVersion, boolean isAsync){
        RestTemplate restClient = getRestClient();
        restClient.setRequestFactory(new Netty4ClientHttpRequestFactory(httpVersion));
        return  new PpsHttpUtilExcute(isAsync,restClient );

    }

    /**
     * 创建PhtomJs的请求器
     * @return
     */
    public  static  PpsHttpUtilExcute   createPhantomClient(boolean isAsync){
        RestTemplate restClient = getRestClient();
        restClient.setRequestFactory(new PhantomRequestFactory());
        return  new PpsHttpUtilExcute(isAsync,restClient );
    }


    /**
     * 创建同步的请求器
     * @return
     */
    public   static <T> PpsHttpUtilExcute2_0<T>   createSyncClient2(Class<T> responseType,boolean isAsync){
        return  new PpsHttpUtilExcute2_0(responseType,isAsync);
    }






}
