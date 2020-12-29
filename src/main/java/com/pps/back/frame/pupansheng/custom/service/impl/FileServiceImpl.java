package com.pps.back.frame.pupansheng.custom.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pps.back.frame.pupansheng.custom.config.FileCosUtil;
import com.pps.back.frame.pupansheng.custom.mapper.UploadRecordMapper;
import com.pps.back.frame.pupansheng.custom.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author
 * @discription;
 * @time 2020/12/29 17:21
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    @Autowired
    FileCosUtil fileCosUtil;
    @Autowired
    UploadRecordMapper uploadRecordMapper;


    @Override
    public String initChunckUpload(JSONObject jsonObject) {

        String fileName = jsonObject.getString("fileName");

        String s = fileCosUtil.initUploadChunck(null, fileName);

        return s;
    }
}
