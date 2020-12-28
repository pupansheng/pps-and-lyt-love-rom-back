package com.pps.back.frame.pupansheng.custom.controller;

import com.alibaba.fastjson.JSON;
import com.pps.back.frame.pupansheng.core.authority.security.entity.SysPermisson;
import com.pps.back.frame.pupansheng.core.authority.security.entity.SysRole;
import com.pps.back.frame.pupansheng.core.authority.security.entity.SysUser;
import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysPermissonDao;
import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysRoleDao;
import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysUserDao;
import com.pps.back.frame.pupansheng.core.authority.security.property.MySecurityProperty;
import com.pps.back.frame.pupansheng.core.common.model.Result;
import com.pps.back.frame.pupansheng.core.common.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @discription;
 * @time 2020/8/27 17:07
 */
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    SysPermissonDao sysPermissonDao;
    @Autowired
    SysRoleDao sysRoleDao;
    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    MySecurityProperty mySecurityProperty;

    @RequestMapping("/getLoginInfo")//登陆获得自己登录信息
    public Result getLoginInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username= (String) authentication.getPrincipal();
        Map info=new HashMap<>();
        if(mySecurityProperty.getOpenConfigUser()){

            List<Map> maps = JSON.parseArray(mySecurityProperty.getConfigUser(), Map.class);
            List<Map> userMap = maps.stream().filter(p -> p.get("username").equals(username)).collect(Collectors.toList());
            if(ValidateUtil.isNotEmpty(userMap)){
                SysUser sysUser=new SysUser();
                sysUser.setName((String)userMap.get(0).get("username"));
                sysUser.setPassword((String)userMap.get(0).get("password"));
                List<SysRole> roles=new ArrayList<>();
                SysRole sysRole1=new SysRole();
                sysRole1.setId(1L);
                sysRole1.setName("ROLE_ADMIN");
                roles.add(sysRole1);
                info.put("userInfo",sysUser);
                info.put("roles", roles);
                info.put("permission",new ArrayList<>());
                return  Result.ok(info);
            }



        }

        SysUser sysUser =new SysUser() ;
        sysUser.setName(username);
        SysUser sysUser1 = sysUserDao.queryCondition(sysUser).get(0);
        sysUser1.setPassword("");
        List<SysRole> sysRoles =sysRoleDao.queryRoleByUserId(sysUser1.getId());
        info.put("userInfo",sysUser1);
        info.put("roles", sysRoles);
        List<SysPermisson> sysPermissons=new ArrayList<>();
        sysRoles.stream().forEach(p->{
            List<SysPermisson> sysPermissons1 = sysPermissonDao.queryPermissonByRoleId(p.getId());
            sysPermissons.addAll(sysPermissons1);
        });
        info.put("permission",sysPermissons);
        return  Result.ok(info);
    }


}
