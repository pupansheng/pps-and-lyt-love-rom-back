package com.pps.back.frame.pupansheng.custom.entity;

import com.pps.back.frame.pupansheng.custom.config.FileCosUtil;
import com.qcloud.cos.model.CompleteMultipartUploadResult;
import com.qcloud.cos.model.PartETag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @discription;
 * @time 2020/12/30 11:34
 */
@Slf4j
public class ChunckFile {

    private String key;
    private String name;
    private String uploadId;
    private String sign;
    private Integer pageSize;
    private List<PartETag> partETags=new ArrayList();
    private volatile  boolean isComplete=false;
    public synchronized void addPartEndTag(PartETag partETag) {
        partETags.add(partETag);
    }
    public synchronized  List<PartETag> getPartETags(){
        return  partETags;
    }
    public synchronized  int getPartHasUploadSize(){
        return  partETags.size();
    }
    public synchronized boolean isComplte(){
        return  partETags.size()==pageSize;
    }
    public synchronized void complete(FileCosUtil fileCosUtil){
        if(!isComplete&&fileCosUtil.searchChunckComplete(key,uploadId).size()==pageSize) {
            log.info("分块上传已经完毕 开始合成");
            CompleteMultipartUploadResult completeMultipartUploadResult = fileCosUtil.compeleteChunckUpload(key, uploadId, partETags);
            log.info("合成完毕！" + completeMultipartUploadResult);
            isComplete=true;
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPartETags(List<PartETag> partETags) {
        this.partETags = partETags;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
