package com.pps.back.frame.pupansheng.core.http.utiil;

import com.pps.back.frame.pupansheng.core.http.myrequest.netty.Netty4ClientHttpRequestFactory;
import com.pps.back.frame.pupansheng.core.http.myrequest.phantom.PhantomClientHttpResponse;
import com.pps.back.frame.pupansheng.core.http.myrequest.phantom.PhantomRequestFactory;
import com.pps.back.frame.pupansheng.core.http.strategy.DefaultContentTypeTextHtmlOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import io.netty.handler.codec.http.HttpVersion;
import org.springframework.http.MediaType;
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
public class PpsHttpUtil {

    private static final int BUFF_SIZE=10;
    private static RestTemplate restTemplates []=new RestTemplate[BUFF_SIZE];
    private static Semaphore semaphore=new Semaphore(BUFF_SIZE);
    private static AtomicBoolean useD []=new AtomicBoolean[BUFF_SIZE];
    static {
        for (int i = 0; i <restTemplates.length ; i++) {
            restTemplates[i]=new RestTemplate();
            useD[i]=new AtomicBoolean(false);
        }
    }


    public static RestTemplate getRestClient(){

        try {
            semaphore.acquire();
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
        return  getRestClient();
    }

    public static void returnClient(){
        semaphore.release();
    }


    public static String autoTransfor2String(ClientHttpResponse clientHttpResponse){

        if(clientHttpResponse instanceof PhantomClientHttpResponse){
           PhantomClientHttpResponse phantomClientHttpResponse= ((PhantomClientHttpResponse) clientHttpResponse);
            String html = phantomClientHttpResponse.getHtml();
            phantomClientHttpResponse.getDriver().close();
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
     * 创建自定义的请求器
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
