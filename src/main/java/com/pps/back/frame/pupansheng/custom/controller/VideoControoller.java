package com.pps.back.frame.pupansheng.custom.controller;

import com.pps.back.frame.pupansheng.core.common.model.Result;
import com.pps.back.frame.pupansheng.custom.entity.UploadRecordPo;
import com.pps.back.frame.pupansheng.custom.mapper.UploadRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author
 * @discription;
 * @time 2020/12/30 17:24
 */
@RestController
@RequestMapping("/jwt/video")
public class VideoControoller {

    @Autowired
    UploadRecordMapper uploadRecordMapper;

    @PostMapping("/list")
    public Result getList(){

        List<UploadRecordPo> uploadRecordPos = uploadRecordMapper.queryAll(null);
       return Result.ok(uploadRecordPos);
    }


}
