package com.pps.back.frame.pupansheng.custom.entity;

import java.util.Date;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
* (UserInfo)实体类
*
* @author default
* @since 2021-01-05 11:28:32
*/
public class UserInfoPo  implements Serializable {
private static final long serialVersionUID = -12468817133391798L;
    
private Integer id;
    
private Integer userId;
    
private String headImage;
    
private String sex;
    
private String address;
    
private Integer age;
    
private Integer lever;
    
private Date optTime;
    
private Integer flag;

    
public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}
    
public Integer getUserId() {
return userId;
}

public void setUserId(Integer userId) {
this.userId = userId;
}
    
public String getHeadImage() {
return headImage;
}

public void setHeadImage(String headImage) {
this.headImage = headImage;
}
    
public String getSex() {
return sex;
}

public void setSex(String sex) {
this.sex = sex;
}
    
public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}
    
public Integer getAge() {
return age;
}

public void setAge(Integer age) {
this.age = age;
}
    
public Integer getLever() {
return lever;
}

public void setLever(Integer lever) {
this.lever = lever;
}
    
public Date getOptTime() {
return optTime;
}

public void setOptTime(Date optTime) {
this.optTime = optTime;
}
    
public Integer getFlag() {
return flag;
}

public void setFlag(Integer flag) {
this.flag = flag;
}
        @Override
    public String toString() {
    return "UserInfo{" +
               ",id ='" + id + '\'' +
               ",userId ='" + userId + '\'' +
               ",headImage ='" + headImage + '\'' +
               ",sex ='" + sex + '\'' +
               ",address ='" + address + '\'' +
               ",age ='" + age + '\'' +
               ",lever ='" + lever + '\'' +
               ",optTime ='" + optTime + '\'' +
               ",flag ='" + flag + '\'' +
            '}';
    }

}