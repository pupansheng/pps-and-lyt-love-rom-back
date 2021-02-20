package com.pps.back.frame.pupansheng.custom.pachong;

import com.alibaba.fastjson.JSONObject;
import com.pps.back.frame.pupansheng.core.common.util.RegexUtil;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpRequstOperation;
import com.pps.back.frame.pupansheng.core.http.strategy.HttpStrategyFactory;
import com.pps.back.frame.pupansheng.core.http.utiil.PpsHttpUtil;
import com.pps.back.frame.pupansheng.custom.pachong.entity.Html;
import com.pps.back.frame.pupansheng.custom.pachong.entity.PlayLink;
import com.pps.back.frame.pupansheng.custom.pachong.entity.SearchVideo;
import org.springframework.boot.SpringApplication;
import org.springframework.http.MediaType;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author
 * @discription;
 * @time 2021/1/21 10:06
 */
public class SearchPage  {


    private static String searUrl="https://www.613767.com/search/-------------.html";
    private static String playUrl="https://www.613767.com";

    public static void main(String[] args) {

        ResourceCatchService resourceCatchService=new _aikekeResouce();
        List<SearchVideo> t = resourceCatchService.getSearchResult("盗梦空间");
        for (SearchVideo searchVideo:t){
            String link = searchVideo.getLink();
            List<PlayLink> playLinks = resourceCatchService.videoResource(link);
            playLinks.stream().forEach(p->{
                String videoUrl = resourceCatchService.getVideoUrl(p.getUrl());
                System.out.println(videoUrl);
            });
        }
        System.out.println(t);


    }
}
