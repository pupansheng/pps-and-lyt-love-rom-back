package com.pps.back.frame.pupansheng.custom.controller;

import com.alibaba.fastjson.JSONObject;
import com.pps.back.frame.pupansheng.core.common.model.Result;
import com.pps.back.frame.pupansheng.custom.config.FileCosUtil;
import com.pps.back.frame.pupansheng.custom.db.GlobalDb;
import com.pps.back.frame.pupansheng.custom.service.FileService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.PartListing;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.UUID;

@RestController("/jwt/file/")
public class uploadController extends BaseController {


    @Autowired
    FileCosUtil fileCosUtil;
    @Autowired
    GlobalDb globalDb;
    @Autowired
    FileService fileService;



    @PostMapping
    public void uploadFile(MultipartFile file,Integer pageNumer,String fileName){

        String loginUerName = getLoginUerName();
        String k=loginUerName+":"+fileName;
        Object s = globalDb.getValue(k);
        //第一页
        if(pageNumer==1){
            if(s!=null){
                //说明之前已经上传过 还没有上传完 搜索模块
                PartListing partListing = fileCosUtil.searchChunck(null, fileName, (String) s);
                System.out.println(partListing);
            }else {
                String uoloadId = fileCosUtil.initUploadChunck(null, fileName);
                globalDb.putValue(k,uoloadId);
            }

        }



    }
    @PostMapping("/chunck/init")
    @ResponseBody
    public Result initChunckFile(@RequestBody JSONObject jsonObject){
        String fileName = jsonObject.getString("fileName");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= (String) authentication.getPrincipal();
        String k=username+":"+fileName;
        Object s = globalDb.getValue(k);
        if(s!=null){
            //说明之前已经上传过 还没有上传完 搜索模块
            PartListing partListing = fileCosUtil.searchChunck(null, fileName, (String) s);
            System.out.println(partListing);
        }else {
            String uoloadId = fileCosUtil.initUploadChunck(null, fileName);
            return Result.ok(uoloadId);
        }
      return  Result.ok(null);
    }

}
