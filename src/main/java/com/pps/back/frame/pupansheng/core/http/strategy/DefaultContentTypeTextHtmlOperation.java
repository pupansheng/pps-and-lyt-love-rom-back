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
 * @discription; 默认的jcontent-type=text/html; charset=UTF-8  返回请求处理
 * @time 2021/1/11 15:22
 */
public class DefaultContentTypeTextHtmlOperation implements HttpRequstOperation {
    @Override
    public Object extractData(ClientHttpResponse response, Class returnType)  {

        String HTML= null;
        try {
            MediaType contentType = response.getHeaders().getContentType();
            byte[] bytes = encode(response);
            Charset charset = contentType.getCharset();
            if(charset==null){
                charset=Charset.forName("utf-8");
            }
            HTML=new String(bytes, charset);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(returnType==String.class){
            return HTML;
        }
        return JSON.parseObject(HTML,returnType);

    }
}
