package com.pps.back.frame.pupansheng.core.http.strategy;

import com.pps.back.frame.pupansheng.core.http.model.PostType;
import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 * @discription;
 * @time 2021/1/11 14:06
 */
public class HttpStrategyFactory {

 private static ConcurrentHashMap<Object,HttpRequstOperation> strates=new ConcurrentHashMap<>();
 private static final String DEFAULT_OPERATION="default_operation";
 //todo 注册默认的处理器
 static {
     strates.put(PostType.APPLICATION_JSON,new DefaulJsonRequestOperation());
     strates.put("application/json",new DefaultContentTypeForJsonOperation());
     strates.put("application/json;charset=UTF-8",new DefaultContentTypeForJsonOperation());
     strates.put("application/x-www-form-urlencoded",new DefaulXfromRequestOperation());
     strates.put("text/plain",new DefaultContentTypeForJsonOperation());
     strates.put("text/plain;charset=UTF-8",new DefaultContentTypeForJsonOperation());
     strates.put("text/html",new DefaultContentTypeTextHtmlOperation());
     strates.put("text/html;charset=UTF-8",new DefaultContentTypeTextHtmlOperation());
     strates.put("text/html;charset=utf-8",new DefaultContentTypeTextHtmlOperation());
 }

 public static void register(Object k,HttpRequstOperation httpResponseOperation){
     strates.put(k,httpResponseOperation);
 }

 public static HttpRequstOperation getOperation(Object k){
     return strates.get(k);
 }

 public static void registerDefaultOperation(HttpRequstOperation httpResponseOperation){
     strates.put(DEFAULT_OPERATION,httpResponseOperation);
 }
 public static HttpRequstOperation getDefaultOperation(){
     HttpRequstOperation httpRequstOperation = strates.get(DEFAULT_OPERATION);
     if(httpRequstOperation==null){
         DefaultHeaderOperation defaultHeaderOperation = new DefaultHeaderOperation();
         strates.put(DEFAULT_OPERATION,defaultHeaderOperation);
         return defaultHeaderOperation;
     }
    return httpRequstOperation;
 }
}
