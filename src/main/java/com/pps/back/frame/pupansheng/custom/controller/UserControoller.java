package com.pps.back.frame.pupansheng.custom.controller;

import com.pps.back.frame.pupansheng.core.common.model.Result;
import com.pps.back.frame.pupansheng.custom.entity.UserInfoPo;
import com.pps.back.frame.pupansheng.custom.im.entity.ImUser;
import com.pps.back.frame.pupansheng.custom.im.listener.CustomListener;
import com.pps.back.frame.pupansheng.custom.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @discription;
 * @time 2021/1/5 11:49
 */
@RestController
@RequestMapping("/jwt/user/")
public class UserControoller {

    @Autowired
    UserInfoMapper userInfoMapper;


    @PostMapping("update")
    public Result update(@RequestBody UserInfoPo userInfoPo){
        Integer userId = userInfoPo.getUserId();
        UserInfoPo query =new UserInfoPo() ;
        query.setUserId(userId);
        List<UserInfoPo> userInfoPos = userInfoMapper.queryAll(query);
        if(userInfoPos.size()>0){
            userInfoPo.setId(userInfoPos.get(0).getId());
            userInfoMapper.update(userInfoPo);
        }else {
            userInfoMapper.insert(userInfoPo);
        }
        return    Result.ok(userInfoPo);
    }






}
