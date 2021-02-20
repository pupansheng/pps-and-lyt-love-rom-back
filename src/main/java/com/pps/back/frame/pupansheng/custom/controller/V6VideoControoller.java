package com.pps.back.frame.pupansheng.custom.controller;

import com.alibaba.fastjson.JSONObject;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.ServiceException;
import com.pps.back.frame.pupansheng.core.common.model.Result;
import com.pps.back.frame.pupansheng.custom.entity.UploadRecordPo;
import com.pps.back.frame.pupansheng.custom.mapper.UploadRecordMapper;
import com.pps.back.frame.pupansheng.custom.pachong.HtmlAnaly;
import com.pps.back.frame.pupansheng.custom.pachong.ResourceCatchService;
import com.pps.back.frame.pupansheng.custom.pachong.entity.ResourceStategy;
import com.pps.back.frame.pupansheng.custom.pachong.entity.SearchVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author
 * @discription;
 * @time 2020/12/30 17:24
 */
@RestController
@RequestMapping("/jwt/v6")
public class V6VideoControoller extends BaseController {

    @Autowired
    HtmlAnaly htmlAnaly;

    @PostMapping("/source")
    public Result getSource(){

        return  Result.ok(ResourceStategy.getKeys());

    }
    @PostMapping("/search")
    public Result getList(@RequestBody JSONObject jsonObject){
        ResourceCatchService resourceCatchService = ResourceStategy.get(jsonObject.getString("source"));
        List<SearchVideo> keyword = resourceCatchService.getSearchResult(jsonObject.getString("keyword"));
        if(keyword.size()==0){
            throw new ServiceException("没有搜索到内容");
        }
        return  Result.ok(keyword);

    }
    @PostMapping("/getPlayList")
    public Result add(@RequestBody JSONObject jsonObject){
        String link = jsonObject.getString("link");
        ResourceCatchService resourceCatchService = ResourceStategy.get(jsonObject.getString("source"));
        return Result.ok(resourceCatchService.videoResource(link));

    }
    @PostMapping("/getHttpLink")
    public Result delete(@RequestBody JSONObject jsonObject){
        ResourceCatchService resourceCatchService = ResourceStategy.get(jsonObject.getString("source"));
        String resourceId = resourceCatchService.getVideoUrl(jsonObject.getString("resourceId"));
        return Result.ok(resourceId);
    }
    @PostMapping("/analy")
    public Result anltHtml(@RequestBody JSONObject jsonObject){

        return Result.ok(htmlAnaly.toOutHtml(jsonObject.getString("url"),jsonObject.getJSONObject("header")));
    }
}
