package com.pps.back.frame.pupansheng.custom.pachong;

import com.alibaba.fastjson.JSONObject;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import com.pps.back.frame.pupansheng.core.http.utiil.PpsHttpUtil;
import com.pps.back.frame.pupansheng.custom.pachong.entity.Html;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author
 * @discription;
 * @time 2021/1/25 10:40
 */
@Service
public class HtmlAnaly {

    public Html toOutHtml(String url, JSONObject hearder){

        Html html=new Html();
        PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h->{
           hearder.keySet().forEach(p->{
               h.set(p,hearder.getString(p));
           });
           html.setRquestHeader(hearder.toJSONString());
        }).get((r)-> {


            try {
                html.setStatusCode(r.getStatusCode().value());
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaType contentType = r.getHeaders().getContentType();
            html.setResponseHeader(r.getHeaders().toString());
            HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
            if(operation!=null) {
                Object o = operation.extractData(r, String.class);
                html.setHtml((String)o);
            }



        });

        return  html;
    }


}
