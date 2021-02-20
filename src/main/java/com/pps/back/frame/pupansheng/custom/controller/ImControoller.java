package com.pps.back.frame.pupansheng.custom.controller;

import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysUserDao;
import com.pps.back.frame.pupansheng.core.common.model.Result;
import com.pps.back.frame.pupansheng.custom.im.entity.ImUser;
import com.pps.back.frame.pupansheng.custom.im.listener.CustomListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/jwt/im")
public class ImControoller {

    @Autowired
    SysUserDao sysUserDao;


    @PostMapping("/get/online")
    public Result getOnline(){
     List<ImUser> array=new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= (String) authentication.getPrincipal();
        CustomListener.LOGIN_USER.forEach((k,v)->{
            if(!k.equals(username)){
                array.add(v);
            }
        });
     return    Result.ok(array);

    }




}
