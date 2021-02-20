package com.pps.back.frame.pupansheng.core.http.strategy;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author pps
 * @discription; 默认的jcontent-type=application/json  返回请求处理
 * @time 2021/1/11 15:22
 */
public class DefaultContentTypeForJsonOperation implements HttpRequstOperation {
    @Override
    public Object extractData(ClientHttpResponse response, Class returnType) {

        MediaType contentType = response.getHeaders().getContentType();
        byte [] bytes= new byte[0];
        try {
            bytes = IOUtils.toByteArray(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Charset charset = contentType.getCharset();
        if(charset==null){
            charset=Charset.forName("utf-8");
        }
        String s = new String(bytes, charset);
        if(returnType==String.class){
            return s;
        }
        return JSON.parseObject(s,returnType);

    }
}
