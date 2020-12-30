package com.pps.back.frame.pupansheng.custom.entity;

import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
* (UploadRecord)实体类
*
* @author default
* @since 2020-12-30 17:46:17
*/
public class UploadRecordPo  implements Serializable {
private static final long serialVersionUID = 250150923260118014L;
    
private String id;
    
private String key;
    
private String url;
    
private Integer userId;
    
private String fileName;
    
private String suffix;
    
private Date optTime;

    
public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}
    
public String getKey() {
return key;
}

public void setKey(String key) {
this.key = key;
}
    
public String getUrl() {
return url;
}

public void setUrl(String url) {
this.url = url;
}
    
public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}
    
public String getFileName() {
return fileName;
}

public void setFileName(String fileName) {
this.fileName = fileName;
}
    
public String getSuffix() {
return suffix;
}

public void setSuffix(String suffix) {
this.suffix = suffix;
}
    
public Date getOptTime() {
return optTime;
}

public void setOptTime(Date optTime) {
this.optTime = optTime;
}
        @Override
    public String toString() {
    return "UploadRecord{" +
               ",id ='" + id + '\'' +
               ",key ='" + key + '\'' +
               ",url ='" + url + '\'' +
               ",userId ='" + userId + '\'' +
               ",fileName ='" + fileName + '\'' +
               ",suffix ='" + suffix + '\'' +
               ",optTime ='" + optTime + '\'' +
            '}';
    }

}