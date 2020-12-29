package com.pps.back.frame.pupansheng.custom.entity;

import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
* (UploadRecord)实体类
*
* @author default
* @since 2020-12-29 17:14:59
*/
public class UploadRecordPo  implements Serializable {
private static final long serialVersionUID = 436192933832665850L;
    
private String id;
    
private String fileName;
    
private String suffix;
    
private Date optTime;

    
public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
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
               ",fileName ='" + fileName + '\'' +
               ",suffix ='" + suffix + '\'' +
               ",optTime ='" + optTime + '\'' +
            '}';
    }

}