package com.pps.back.frame.pupansheng.custom.pachong;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.ServiceException;
import com.pps.back.frame.pupansheng.core.common.util.RegexUtil;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import com.pps.back.frame.pupansheng.core.http.utiil.PpsHttpUtil;
import com.pps.back.frame.pupansheng.custom.pachong.entity.PlayLink;
import com.pps.back.frame.pupansheng.custom.pachong.entity.ResourceStategy;
import com.pps.back.frame.pupansheng.custom.pachong.entity.SearchVideo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author
 * @discription; 琪琪电影网爬取资源
 * @time 2021/1/21 17:06
 */
@Service
public class _74NewResouce implements ResourceCatchService, InitializingBean {

    private String searchUrl="http://www.yingwangtv.com/search";
    private static String playUrl="http://www.yc2050.com";
    @Override
    public String getVideoUrl(String url) {

        AtomicReference<String> re=new AtomicReference<>();
        PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h->{
            h.set("Referer","http://www.yc2050.com/");
            h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");

        }).get((r)-> {
            MediaType contentType = r.getHeaders().getContentType();
            HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
            Object o = operation.extractData(r, String.class);
            String html = (String) o;
            String oneContentByStartAndEnd = RegexUtil.findOneContentByStartAndEnd("marginwidth=\"0\" scrolling=\"no\" src=\"", "\"", html);
            if(!oneContentByStartAndEnd.contains("url=")) {
                String v = oneContentByStartAndEnd;
                int v1 = v.indexOf("?");
                String prefix = v.substring(0, v1 - 1);
                String suffix = v.substring(v1 + 1);
                String n = prefix + "/video.php/?" + suffix;
                PpsHttpUtil.createSyncClient().setUrl(n).setHttpHeadersConsumer(h -> {
                    h.set("Referer", "http://www.yc2050.com/");
                    h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
                }).get((r2) -> {

                    MediaType contentType2 = r2.getHeaders().getContentType();
                    HttpRequstOperation operation2 = HttpStrategyFactory.getOperation(contentType2.toString());
                    Object o2 = operation2.extractData(r2, String.class);
                    String html2 = (String) o2;
                    Map map = JSON.parseObject(html2, Map.class);
                    re.set((String) map.get("url"));


                });
            }else if(oneContentByStartAndEnd.contains("url=")){

                PpsHttpUtil.createSyncClient().setUrl(oneContentByStartAndEnd).setHttpHeadersConsumer(h -> {
                    h.set("Referer", "http://www.yc2050.com/");
                    h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
                }).get((r2) -> {

                    MediaType contentType2 = r2.getHeaders().getContentType();
                    HttpRequstOperation operation2 = HttpStrategyFactory.getOperation(contentType2.toString());
                    Object o2 = operation2.extractData(r2, String.class);
                    String html2 = (String) o2;
                    String in=  RegexUtil.findOneContentByStartAndEnd("var vid=\"","\"",html2);
                    re.set(in);


                });


            }
        });
       if(re.get()==null){
           throw new ServiceException("获取播放连接失败！");
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
            List<String> manyContentByStartAndEnd = RegexUtil.findManyContentByStartAndEnd("<li class=\"vbox\"><a class=\"vbox_t\"", "</li>", h);
                manyContentByStartAndEnd.stream().forEach(p->{
                SearchVideo searchVideo=new SearchVideo();
                String name=   RegexUtil.findOneContentByStartAndEnd("title=\"","\"",p);
                String image= RegexUtil.findOneContentByStartAndEnd("url\\(","\\);",p);
                String link= RegexUtil.findOneContentByStartAndEnd(" href=\"","\"",p);
                link=playUrl+link;
                String deatail= RegexUtil.findOneContentByStartAndEnd("<h4><a href=\"/show/50755.html\"","</a></h4>",p);
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
            h.set("Referer","http://www.yc2050.com/search");
            h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
        }).setStrict(true).get((r)-> {
            MediaType contentType = r.getHeaders().getContentType();
            HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
            Object o = operation.extractData(r, String.class);
            String html = (String) o;
            List<String> list= RegexUtil.findManyContentByStartAndEnd("<li><a href=\"","\">",html);
            String list2= RegexUtil.findOneContentByStartAndEnd("<ul class=\"list_block show\">","</ul>",html);
            List<String> tt=  RegexUtil.findManyContentByStartAndEnd("\">","</a>",list2);
            for (int i = 0; i <list.size() ; i++) {
                String t=list.get(i);
                PlayLink playLink=new PlayLink();
                t="http://www.yc2050.com"+t;
                playLink.setUrl(t);
                playLink.setName(tt.get(i));
                playLinks.add(playLink);
            }
        });

        playLinks.forEach(p->{

            String url = p.getUrl();
            PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h->{
                h.set("Referer","http://www.yc2050.com/");
                h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
            }).get((r)-> {
                MediaType contentType = r.getHeaders().getContentType();
                HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
                Object o = operation.extractData(r, String.class);
                String html = (String) o;
                String oneContentByStartAndEnd = RegexUtil.findOneContentByStartAndEnd("<iframe border=\"0\" frameborder=\"0\" height=\"zzs20\" marginheight=\"0\" marginwidth=\"0\" scrolling=\"no\" src=\"", "\"", html);
                oneContentByStartAndEnd= oneContentByStartAndEnd.replace("wap123","sapi");
                oneContentByStartAndEnd=oneContentByStartAndEnd.replaceAll("zzs..","100%");
                p.setUrl(oneContentByStartAndEnd);
            });

        });

        return playLinks;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceStategy.register("新骑士影院",this);
    }
}
