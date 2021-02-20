package com.pps.back.frame.pupansheng.core.http.utiil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * @author
 * @discription;
 * @time 2021/1/21 10:12
 */
public class UrlParamGenarete {

    /**
     * 根据参数 生成对应的url
     * @param param
     * @return
     */
    public static String urlParamByMap(Map<Object,Object> param,boolean add_w){

        Set<Object> strings = param.keySet();
        String urlParam="";
        int count=0;
        for (Object key:strings){
            Object o = param.get(key);
            if(count==0) {
                try {
                    if(o instanceof String) {
                        urlParam = urlParam + key + "=" + URLEncoder.encode((String) o, "UTF-8");
                    }else {
                        urlParam = urlParam +key+"="+ o;
                    }
                } catch (UnsupportedEncodingException e) {
                    urlParam = urlParam +key+"="+ o;
                }
            }else {
                try {
                    if(o instanceof String) {
                        urlParam = urlParam + "&" + key + "=" + URLEncoder.encode((String) o, "UTF-8");
                    }else {
                        urlParam = urlParam +"&"+key+"="+ o;
                    }
                } catch (UnsupportedEncodingException e) {
                    urlParam = urlParam +"&"+key+"="+ o;
                }
            }
            count++;
        }
        if(add_w){
            urlParam="?"+urlParam;
        }
        return  urlParam;

    }

}
