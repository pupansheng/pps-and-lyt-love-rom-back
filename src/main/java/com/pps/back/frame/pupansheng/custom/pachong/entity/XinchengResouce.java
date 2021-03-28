package com.pps.back.frame.pupansheng.custom.pachong.entity;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.ServiceException;
import com.pps.back.frame.pupansheng.core.common.util.RegexUtil;
import com.pps.back.frame.pupansheng.core.http.strategy.DefaultContentTypeTextHtmlOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import com.pps.back.frame.pupansheng.core.http.utiil.PpsHttpUtil;
import com.pps.back.frame.pupansheng.custom.pachong.ResourceCatchService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author
 * @discription; 琪琪电影网爬取资源
 * @time 2021/1/21 17:06
 */
@Service
public class XinchengResouce implements ResourceCatchService, InitializingBean {

    private String searchUrl="http://www.hnxyyy.cn/vodsearch/%E4%BD%A0%E5%A5%BD%EF%BC%8C%E6%9D%8E%E7%84%95%E8%8B%B1-------------/";
    private static String playUrl="http://www.hnxyyy.cn";
    @Override
    public String getVideoUrl(String url) {

        String[] split = url.split("#");
        String parse=split[0];
        String urlBase=split[1];
        Map map1 = JSON.parseObject(parse, Map.class);

        Set sets = map1.keySet();
        for (Object host:sets){

            Map v = (Map)map1.get(host);
            String baseU=  (String) v.get("parse");
            if(baseU==null||baseU.equals("")){
                continue;
            }
            String newUrl=baseU+urlBase;
            String url1 = getUrl(newUrl);
            if(url1!=null&&!"".equals(url1)&&test(url1)){
                return url1;
            }
        }

        throw new RuntimeException("该资源未找到播放资源！");
    }
    public boolean test(String url){
        try{
            PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h->{
                h.set("Referer",playUrl);
                h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
            }).get((r)-> {

            });
            return  true;
        }catch (Exception e){
            System.out.println(url+":"+"影片资源不可用！");
        }
        return  false;
    }
    public String getUrl(String url){

        AtomicReference<String> re=new AtomicReference<>();
        try {


            PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h -> {
                h.set("Referer", playUrl);
                h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
            }).get((r) -> {
                MediaType contentType = r.getHeaders().getContentType();
                HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
                Object o = operation.extractData(r, String.class);
                String html = (String) o;
                String oneContentByStartAndEnd = RegexUtil.findOneContentByStartAndEnd("var url = '", "'", html);
                re.set(oneContentByStartAndEnd);
            });
        }catch (Exception e){
            System.out.println(url+"：网址不可用！");
        }
        return re.get();

    }
    @Override
    public  List<SearchVideo> getSearchResult(String word) {
        Map param=new HashMap<>();
        param.put("wd",word);
        List<SearchVideo> searchVideos=new ArrayList<>();
        PpsHttpUtil.createSyncClient().setUrl(searchUrl).setStrict(false).set_3xxStrategy((r,p)->{
            return  p;
        }).setParam(null,param).setAddW(false).postXWFrom((r)->{

            Object o = HttpStrategyFactory.getOperation(r.getHeaders().getContentType().toString()).extractData(r, String.class);
            String h=(String)o;
            String uls = RegexUtil.findOneContentByStartAndEnd("<ul class=\"myui-vodlist__media clearfix\" id=\"searchList\">", "</ul>", h);
            List<String> lis = RegexUtil.findManyContentByStartAndEnd("<li class=\"clearfix\">", "</li>", uls);
            lis.stream().forEach(p->{
                SearchVideo searchVideo=new SearchVideo();
                String name=RegexUtil.findOneContentByStartAndEnd("<a class=\"searchkey\"","/a>",p);
                name= RegexUtil.findOneContentByStartAndEnd(">","<",name);
                String image= RegexUtil.findOneContentByStartAndEnd("data-original=\"","\"",p);
                String link= RegexUtil.findOneContentByStartAndEnd("<div class=\"detail\">","</div>",p);
                link= RegexUtil.findOneContentByStartAndEnd("<p class=\"margin-0\">","</p>",link);
                link=RegexUtil.findOneContentByStartAndEnd("href=\"","\"",link);
                link=playUrl+link;
                String deatail= RegexUtil.findOneContentByStartAndEnd("<p class=\"hidden-xs\">","</p>",p);
                searchVideo.setDetail(deatail);
                searchVideo.setImage(image);
                searchVideo.setLink(link);
                searchVideo.setName(name);
                searchVideos.add(searchVideo);
            });
        });

        return searchVideos;
    }

    @Override
    public List<PlayLink> videoResource(String link) {
        List<PlayLink> playLinks=new ArrayList<>();
        PpsHttpUtil.createSyncClient().setUrl(link).setHttpHeadersConsumer((h)->{
            h.set("Referer",playUrl);
          //  h.set("Accept-Language","zh-CN,zh;q=0.9");
          //  h.set("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
           //h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
        }).setStrict(true).get((r)-> {
            MediaType contentType = r.getHeaders().getContentType();
            HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
            Object o = operation.extractData(r, String.class);
            String html = (String) o;
            String res= RegexUtil.findOneContentByStartAndEnd("<ul class=\"myui-content__list playlist clearfix\" id=\"playlist\">","</ul>",html);
            List<String> list = RegexUtil.findManyContentByStartAndEnd("<li", "</li>", res);
            for (int i = 0; i <list.size() ; i++) {
                String t=RegexUtil.findOneContentByStartAndEnd("href=\"","\"",list.get(i));
                PlayLink playLink=new PlayLink();
                t=playUrl+t;
                playLink.setUrl(t);
                String name=RegexUtil.findOneContentByStartAndEnd("href=\"","/a",list.get(i));
                name = RegexUtil.findOneContentByStartAndEnd(">", "<", name);
                playLink.setName(name);
                playLinks.add(playLink);
            }
        });

        playLinks.forEach(p->{

            String url = p.getUrl();
            PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h->{
                h.set("Referer",playUrl);
                h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
            }).get((r)-> {
                MediaType contentType = r.getHeaders().getContentType();
                HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
                Object o = operation.extractData(r, String.class);
                String html = (String) o;
                String data = RegexUtil.findOneContentByStartAndEnd("var player_data=", "<", html);
                Map map = JSON.parseObject(data, Map.class);
                String https = (String) map.get("url");
                String scriptUrl = RegexUtil.findOneContentByStartAndEnd("<div class=\"embed-responsive clearfix\">", "</div>", html);
                scriptUrl = RegexUtil.findOneContentByStartAndEnd("src=\"", "\"", scriptUrl);
                scriptUrl=playUrl+scriptUrl;
                String script = getScript(scriptUrl);
                String jsonParse = RegexUtil.findOneContentByStartAndEnd("MacPlayerConfig.player_list=", ",MacPlayerConfig.downer_list", script);
                p.setUrl(jsonParse+"#"+https);
            });
//https://www.mycqzx.com:65/20190802/3gKrlVkP/index.m3u8
        });

        return playLinks;
    }

    public String getScript(String url){
        AtomicReference<String> hr=new AtomicReference<>();
        PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h->{
            h.set("Referer",playUrl);
            h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
        }).get((r)-> {
            HttpRequstOperation operation = new DefaultContentTypeTextHtmlOperation();
            Object o = operation.extractData(r, String.class);
            String html = (String) o;
            hr.set(html);
        });
        
        return hr.get();
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceStategy.register("星辰影院",this);
    }
    public static void main(String args[]){

    }
}
