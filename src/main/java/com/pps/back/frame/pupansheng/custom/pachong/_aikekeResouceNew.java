package com.pps.back.frame.pupansheng.custom.pachong;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.ServiceException;
import com.pps.back.frame.pupansheng.core.common.util.RegexUtil;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import com.pps.back.frame.pupansheng.core.http.utiil.PpsHttpUtil;
import com.pps.back.frame.pupansheng.custom.pachong.ResourceCatchService;
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
 * @discription; 爱爱克电影网爬取资源
 * @time 2021/1/21 17:06
 */
@Service
public class _aikekeResouceNew implements ResourceCatchService, InitializingBean {

    private static String searchUrl="https://www.613767.com/search";
    private static String playUrl="https://www.613767.com";
    @Override
    public String getVideoUrl(String url) {
        AtomicReference<String> re=new AtomicReference<>();
        PpsHttpUtil.createSyncClient().setUrl(url).setHttpHeadersConsumer(h->{
            h.set("Referer","https://www.613767.com/");
            h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
        }).get((r)-> {
            MediaType contentType = r.getHeaders().getContentType();
            HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
            Object o = operation.extractData(r, String.class);
            String html = (String) o;
            String oneContentByStartAndEnd = RegexUtil.findOneContentByStartAndEnd("<div class=\"mplayer\">", "</script>", html);
            oneContentByStartAndEnd=RegexUtil.findOneContentByStartAndEnd("src=\"","\"",oneContentByStartAndEnd);
            if(oneContentByStartAndEnd.contains("?v=")) {
                String v = oneContentByStartAndEnd;
                PpsHttpUtil.createSyncClient().setUrl(v).setHttpHeadersConsumer(h -> {
                    h.set("Referer","https://www.613767.com/");
                    h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
                }).get((r2) -> {
                    MediaType contentType2 = r2.getHeaders().getContentType();
                    HttpRequstOperation operation2 = HttpStrategyFactory.getOperation(contentType2.toString());
                    Object o2 = operation2.extractData(r2, String.class);
                    String html2 = (String) o2;
                    String url2=RegexUtil.findOneContentByStartAndEnd("var vid=\"","\"",html2);
                    if(url2.equals("")){

                        int v1 = v.indexOf("?");
                        String prefix = v.substring(0, v1 - 1);
                        String suffix = v.substring(v1 + 1);
                        String n = prefix + "/video.php/?" + suffix;
                        PpsHttpUtil.createSyncClient().setUrl(n).setHttpHeadersConsumer(h -> {
                            h.set("Referer","https://www.613767.com/");
                            h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
                        }).get((r22) -> {
                            MediaType contentType22 = r22.getHeaders().getContentType();
                            HttpRequstOperation operation22 = HttpStrategyFactory.getOperation(contentType22.toString());
                            Object o22 = operation22.extractData(r22, String.class);
                            String html22 = (String) o22;
                            Map map = JSON.parseObject(html22, Map.class);
                            re.set((String) map.get("url"));
                        });
                    }else {
                        re.set((url2));
                    }

                });
            }else if(oneContentByStartAndEnd.contains("url=")){

                String v = oneContentByStartAndEnd;
                PpsHttpUtil.createSyncClient().setUrl(playUrl+v).setHttpHeadersConsumer(h -> {
                    h.set("Referer","https://www.613767.com/");
                    h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
                }).get((r2) -> {
                    MediaType contentType2 = r2.getHeaders().getContentType();
                    HttpRequstOperation operation2 = HttpStrategyFactory.getOperation(contentType2.toString());
                    Object o2 = operation2.extractData(r2, String.class);
                    String html2 = (String) o2;
                    String t2 = RegexUtil.findOneContentByStartAndEnd("src=\"", "\"", html2);
                    PpsHttpUtil.createSyncClient().setUrl(t2).setHttpHeadersConsumer(h -> {
                        h.set("Referer","https://www.613767.com/");
                        h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
                    }).get((r3) -> {
                        MediaType contentType3 = r3.getHeaders().getContentType();
                        HttpRequstOperation operation3 = HttpStrategyFactory.getOperation(contentType3.toString());
                        Object o3 = operation3.extractData(r3, String.class);
                        String html3 = (String) o3;
                        String  link= RegexUtil.findOneContentByStartAndEnd("var vid=\"", "\"", html3);
                        re.set(link);
                    });



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
        Map map=new HashMap<>();
        map.put("wd",word);
        List<SearchVideo> searchVideos=new ArrayList<>();
        PpsHttpUtil.createSyncClient().setUrl(searchUrl).setAddW(true).setHttpHeadersConsumer((h)->{
            h.set("Referer","https://www.613767.com/");
            h.set("user-agent","Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
        }).setParam(map).postXWFrom((r)->{
            HttpRequstOperation operation = HttpStrategyFactory.getOperation(r.getHeaders().getContentType().toString());
            Object o = operation.extractData(r, String.class);
            String html= (String)o;
            String ul = RegexUtil.findOneContentByStartAndEnd("<ul class=\"tbox_m\">", "</ul>", html);
            List<String> lis = RegexUtil.findManyContentByStartAndEnd("<li class", "</li>", ul);
            lis.stream().forEach(p->{
                SearchVideo searchVideo=new SearchVideo();
                String name=   RegexUtil.findOneContentByStartAndEnd("title=\"","\"",p);
                String image= RegexUtil.findOneContentByStartAndEnd("background-image: url\\(","\\)",p);
                String link= RegexUtil.findOneContentByStartAndEnd("href=\"","\"",p);
                link=playUrl+link;
                String deatail= name;
                searchVideo.setDetail(deatail);
                searchVideo.setImage(image);
                searchVideo.setLink(link);
                searchVideo.setName(name);
                searchVideos.add(searchVideo);
            });

        });

        return  searchVideos;
    }

    @Override
    public List<PlayLink> videoResource(String linkU) {
        List<PlayLink> playLinks = new ArrayList<>();
        PpsHttpUtil.createSyncClient().setUrl(linkU).setHttpHeadersConsumer((h) -> {
            h.set("Referer", "https://www.613767.com/");
            h.set("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1 Edg/88.0.4324.96");
        }).setStrict(true).get((r) -> {
            MediaType contentType = r.getHeaders().getContentType();
            HttpRequstOperation operation = HttpStrategyFactory.getOperation(contentType.toString());
            Object o = operation.extractData(r, String.class);
            String html = (String) o;

            List<String> rr = RegexUtil.findManyContentByStartAndEnd("<div class=\"tbox tabs\">", "</ul>", html);
            rr.forEach(div -> {
                    String nameP=RegexUtil.findOneContentByStartAndEnd("</i>","</h3>",div);
                    PlayLink playLink = new PlayLink();
                    String allName = nameP;
                    String url = RegexUtil.findOneContentByStartAndEnd("href=\"", "\"",div);
                    url = playUrl + url;
                    playLink.setUrl(url);
                    playLink.setName(allName);
                    playLinks.add(playLink);

            });
        });
        if(playLinks.size()==0){
            throw new ServiceException("播放列表为空！");
        }

        return playLinks;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        ResourceStategy.register("扛把子影城（新）",this);
    }
}
