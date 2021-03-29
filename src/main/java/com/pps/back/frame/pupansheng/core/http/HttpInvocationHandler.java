package com.pps.back.frame.pupansheng.core.http;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.pps.back.frame.pupansheng.core.http.annoation.HttpHeader;
import com.pps.back.frame.pupansheng.core.http.annoation.HttpMapper;
import com.pps.back.frame.pupansheng.core.http.annoation.HttpMethod;
import com.pps.back.frame.pupansheng.core.http.annoation.HttpParam;
import com.pps.back.frame.pupansheng.core.http.model.PostType;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author
 * @discription;
 * @time 2021/1/9 20:19
 */
@Slf4j
public class HttpInvocationHandler implements InvocationHandler {

    private BeanFactory beanFactory;
    private RestTemplate restTemplate;
    private Class mapper;
    private HttpMapper httpMapper;
    private static final String URL_CONCAT="/";
    public HttpInvocationHandler(Class mapper, BeanFactory beanFactory){
        this.mapper=mapper;
        this.httpMapper= (HttpMapper) mapper.getAnnotation(HttpMapper.class);
        this.beanFactory=beanFactory;
            try {
                restTemplate=beanFactory.getBean(RestTemplate.class);
             } catch (BeansException e) {
                RestTemplate restTemplate2 = new RestTemplate();
                restTemplate=restTemplate2;
            }
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //解析url
        String baseUrl = httpMapper.baseUrl();
        HttpHeader[] httpHeaders = httpMapper.headers();
        String httpMethodType=httpMapper.method();
        PostType postType =httpMapper.type();
        HttpMethod annotation = method.getAnnotation(HttpMethod.class);
        Class<?> returnType = method.getReturnType();
        if(annotation!=null){
             httpMethodType= annotation.method();
             postType= annotation.type();
        }

       //todo 解析header
        HttpHeaders headers = new HttpHeaders();
        for (HttpHeader httpHeader:httpHeaders){
            headers.add(httpHeader.name(),httpHeader.value());
        }

        if(annotation!=null){
            if(annotation.overParam()){
                headers.clear();
            }
            for (HttpHeader httpHeader:annotation.headers()){
                headers.add(httpHeader.name(),httpHeader.value());
            }
        }


        //todo 解析携带参数
        Map requestParamJsonBody=new HashMap();
        Map requestParamXform=new HashMap();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int count=0;
        boolean isJsonRequest=httpMethodType.equals("post")&&postType==PostType.APPLICATION_JSON;
        for (Object ab:args){

            HttpParam declaredAnnotation =null;
            if(parameterAnnotations[count].length>0){
             declaredAnnotation= (HttpParam) Arrays.stream(parameterAnnotations[count]).filter(p->p.annotationType()==HttpParam.class).findFirst().orElse(null);
            }

            if(ab instanceof Number ||ab instanceof String){
                if(declaredAnnotation==null) {
                    throw new RuntimeException("警告：" + ab + "请求参数为基本类型和字符串类型 必须携带注解@HttpParam -> 注释参数名称");
                }
                String name = declaredAnnotation.name();
                if("body".equals(declaredAnnotation.paramType())) {
                        requestParamJsonBody.put(name, ab);
                }else {
                    requestParamXform.put(name, ab);
                }
                count++;
                continue;
            }

            if(ab instanceof Map){
                if(declaredAnnotation==null&&isJsonRequest){
                    requestParamJsonBody.putAll((Map)ab);
                }else {
                    if("body".equals(declaredAnnotation.paramType())) {
                        requestParamJsonBody.putAll((Map)ab);
                    }else {
                        requestParamXform.putAll((Map)ab);
                    }
                }
            }else {

                Class<?> aClass = ab.getClass();
                Field[] declaredFields = aClass.getDeclaredFields();
                HttpParam finalDeclaredAnnotation = declaredAnnotation;
                Arrays.stream(declaredFields).forEach(f->{
                    f.setAccessible(true);
                    String name = f.getName();
                    try {
                        Object o = f.get(ab);
                        if(o!=null) {
                            if(finalDeclaredAnnotation ==null&&isJsonRequest){
                                requestParamJsonBody.put(name,o);
                            }else {
                                if("body".equals(finalDeclaredAnnotation.paramType())) {
                                    requestParamJsonBody.put(name,o);
                                }else {
                                    requestParamXform.put(name,o);
                                }

                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException("解析请求参数错误："+e.getMessage());
                    }
                });
            }
            count++;
        }
        //todo 解析请求地址
        String url="";
        if(annotation!=null){
            String urlT = annotation.url();
            if(!urlT.equals("")){
                url=urlT;
            }
        }
        if(url.equals("")){
            // todo 根据方法名采用驼峰转换成请求
            String name = method.getName();
            char[] chars = name.toCharArray();
            for (int i = 0; i <chars.length ; i++) {
                char c=chars[i];
                if(c<=90&&c>=65){
                    url=url+URL_CONCAT+(char)(c+32);
                }else {
                    url+=c;
                }
            }
        }
        if(url.startsWith(URL_CONCAT)){
            if(baseUrl.endsWith(URL_CONCAT)){
                url=url.substring(1);
            }
        }else {
            if(!baseUrl.endsWith(URL_CONCAT)){
                url=URL_CONCAT+url;
            }
        }



        /*
         默认请求头
         */
        HttpRequstOperation defaultOperation = HttpStrategyFactory.getDefaultOperation();
        defaultOperation.defaultHeaderOperation(headers);


        //todo 准备网络请求
        String httpUrl=baseUrl+url+defaultOperation.genereateUrlByParam(requestParamXform);

        if(httpMethodType.equals("post")) {

            PostType finalPostType = postType;
            PostType finalPostType1 = postType;
            return   restTemplate.execute(httpUrl, org.springframework.http.HttpMethod.POST, new RequestCallback() {
                @Override
                public void doWithRequest(ClientHttpRequest request) throws IOException {
                    request.getHeaders().addAll(headers);
                    HttpRequstOperation operation = HttpStrategyFactory.getOperation(finalPostType.getMsg());
                    if(operation!=null){
                        operation.doWithRequest(request,requestParamJsonBody);
                    }
                }
            }, new ResponseExtractor<Object>() {
                @Override
                public Object extractData(ClientHttpResponse response) throws IOException {
                    //TODO 结果解析
                    if(response.getStatusCode()!= HttpStatus.OK){
                        throw new RuntimeException(httpUrl+":http请求失败：");
                    }
                    if(returnType==Void.class){
                        return null;
                    }
                    MediaType contentType = response.getHeaders().getContentType();
                    HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
                    if(operation!=null){
                      return   operation.extractData(response,returnType);
                    }
                    throw new RuntimeException("Content-type:"+contentType+"类型：未提供处理器处理");
                }
            });



        }else if(httpMethodType.equals("get")){

          return   restTemplate.execute(httpUrl, org.springframework.http.HttpMethod.GET, new RequestCallback() {
                @Override
                public void doWithRequest(ClientHttpRequest request) throws IOException {
                    request.getHeaders().addAll(headers);
                }
            }, new ResponseExtractor<Object>() {
                @Override
                public Object extractData(ClientHttpResponse response) throws IOException {
                    //TODO 结果解析
                    if(response.getStatusCode()!= HttpStatus.OK){
                        throw new RuntimeException(httpUrl+":http请求失败：");
                    }
                    if(returnType==Void.class){
                        return null;
                    }
                    MediaType contentType = response.getHeaders().getContentType();
                    HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
                    if(operation!=null){
                        return   operation.extractData(response,returnType);
                    }
                    throw new RuntimeException("Content-type:"+contentType+"类型：未提供处理器处理");
                }
            });

        }else {
            throw new RuntimeException(httpMethodType+"：请求方式未做处理");
        }




    }
    public static InvocationHandler crateProxy(Class mapper, BeanFactory beanFactory){
       return new HttpInvocationHandler(mapper,beanFactory);
    }
}
