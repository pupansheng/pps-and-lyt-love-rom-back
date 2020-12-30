package com.pps.back.frame.pupansheng.custom.controller;

import com.alibaba.fastjson.JSONObject;
import com.pps.back.frame.pupansheng.core.common.model.Result;
import com.pps.back.frame.pupansheng.custom.config.FileCosUtil;
import com.pps.back.frame.pupansheng.custom.db.GlobalDb;
import com.pps.back.frame.pupansheng.custom.entity.ChunckFile;
import com.pps.back.frame.pupansheng.custom.entity.UploadRecordPo;
import com.pps.back.frame.pupansheng.custom.mapper.UploadRecordMapper;
import com.pps.back.frame.pupansheng.custom.service.FileService;
import com.qcloud.cos.model.CompleteMultipartUploadResult;
import com.qcloud.cos.model.PartETag;
import com.qcloud.cos.model.PartListing;
import com.qcloud.cos.model.UploadPartResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/jwt/file")
@Slf4j
public class uploadController extends BaseController {

    private Object lock=new Object();
    @Autowired
    FileCosUtil fileCosUtil;
    @Autowired
    GlobalDb globalDb;
    @Autowired
    FileService fileService;
    @Autowired
    UploadRecordMapper uploadRecordMapper;

    @PostMapping("chunck/process")
    public Result searchProcess(@RequestBody JSONObject jsonObject){
        String key=jsonObject.getString("key");
        String uploadId=jsonObject.getString("uploadId");
        List<PartETag> partETags = fileCosUtil.searchChunckComplete(key, uploadId);
        return Result.ok(partETags.size());

    }
    @PostMapping("chunck/complete")
    public Result complete(@RequestBody JSONObject jsonObject){

        String key=jsonObject.getString("key");
        String uploadId=jsonObject.getString("uploadId");
        ChunckFile value = (ChunckFile) globalDb.getValue(uploadId);
        value.complete(fileCosUtil);
        String url = fileCosUtil.getUrl(key);

        globalDb.deleteValue(uploadId);
        globalDb.deleteValue(value.getSign());



        String name = value.getName();
        UploadRecordPo uploadRecordPo =new UploadRecordPo() ;
        uploadRecordPo.setId(UUID.randomUUID().toString());
        uploadRecordPo.setFileName(name);
        uploadRecordPo.setOptTime(new Date());
        String substring = name.substring(name.lastIndexOf(".") + 1);
        uploadRecordPo.setSuffix(substring);
        uploadRecordPo.setUrl(url);
        uploadRecordPo.setUserId(jsonObject.getIntValue("userId"));
        uploadRecordPo.setKey(key);
        uploadRecordMapper.insert(uploadRecordPo);



        return Result.ok(url);
    }


    @PostMapping("/chunck/upload")
    public Result uploadFile(MultipartFile file,Integer pageNumber,Integer size,String key,String uploadId){
        ChunckFile value = (ChunckFile) globalDb.getValue(uploadId);
        if(value==null){
            throw  new RuntimeException("上传终止");
        }
        List<PartETag> partETags = value.getPartETags();
        boolean isHasUpload=  partETags.stream().anyMatch(p->{
            if(p.getPartNumber()==pageNumber){
                return  true;
            }
            return false;
        });
       if(isHasUpload){
          return Result.ok("已上传");
       }

        try {
            UploadPartResult uploadPartResult = fileCosUtil.uploadFileChunck(uploadId, pageNumber, key,size, file.getInputStream());
            PartETag partETag = uploadPartResult.getPartETag();
            value.addPartEndTag(partETag);
            return Result.ok("已上传");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.err("上传发生错误 已取消");
        }


    }
    @PostMapping("/chunck/init")
    @ResponseBody
    public Result initChunckFile(@RequestBody JSONObject jsonObject){
        Map map=new HashMap();
        String sign = jsonObject.getString("sign");
        String name = jsonObject.getString("name");
        String user = jsonObject.getString("user");
        String userId = jsonObject.getString("userId");
        Integer pageSize = jsonObject.getInteger("pageSize");
        Object value = globalDb.getValue(sign);
        if(value==null){
            //todo 说明此前没有传过这个文件对象
            //创建文件key
            String key=user+"/"+userId+"/"+name;
            log.info("创建文件{}的key：{}",name,key);
            String uploadId = fileCosUtil.initUploadChunck(key);
            log.info("创建文件{}的uploadId：{}",name,uploadId);
            ChunckFile chunckFile=new ChunckFile();
            chunckFile.setKey(key);
            chunckFile.setUploadId(uploadId);
            chunckFile.setName(name);
            chunckFile.setPageSize(pageSize);
            chunckFile.setSign(sign);
            globalDb.putValue(sign,uploadId);
            globalDb.putValue(uploadId,chunckFile);
            map.put("hasUpload",false);
            map.put("uploadId",uploadId);
            map.put("pageNumber",1);
            map.put("key",key);
        }else {//已上传过 但是还未上传完毕

            String uploadID=(String)value;
            ChunckFile chunckFile= (ChunckFile)globalDb.getValue(uploadID);
            String key=chunckFile.getKey();
            map.put("uploadId",uploadID);
            map.put("key",key);
            map.put("pageNumber",1);
            map.put("hasUpload",true);
        }

        return  Result.ok(map);
    }

}
