package com.pps.back.frame.pupansheng.core.http.strategy;

import com.pps.back.frame.pupansheng.core.http.model.PostType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * @author
 * @discription;
 * @time 2021/1/11 14:47
 */
public interface HttpRequstOperation  {

    /**
     * 默认的http处理头信息
     * @param httpHeaders
     */
    default  void  defaultHeaderOperation(HttpHeaders httpHeaders) {};
    /**
     * 对http返回数据的处理
     * @param response
     * @return
     * @throws IOException
     */
    default Object extractData(ClientHttpResponse response,Class returnType) {return response;};


    /**
     * 对请求消息的处理
     * @param request
     * @throws IOException
     */
    default void doWithRequest(ClientHttpRequest request, Map requestParamJsonBody) {}

    /**
     * get请求 url的拼装
     */
    default String genereateUrlByParam(Map requestParamXform ){
        String query="?";
        Set<String> set = requestParamXform.keySet();
        int i=0;
        for(String key:set){
            if(i==0){
                query="?"+key+"="+requestParamXform.get(key);
            }else {
                query=query+"&"+key+"="+requestParamXform.get(key);
            }
            i++;
        }
        return query;
    }

    default byte [] encode(ClientHttpResponse response) throws IOException {

            InputStream body = response.getBody();
            byte[] bytes=null;
            bytes = IOUtils.toByteArray(body);
            return  bytes;

    }

}
