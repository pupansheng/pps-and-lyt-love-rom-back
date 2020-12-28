package com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.form;

import com.alibaba.fastjson.JSON;

import com.pps.back.frame.pupansheng.core.common.util.ValidateUtil;
import com.pps.back.frame.pupansheng.core.authority.security.component.exception.ServiceException;
import com.pps.back.frame.pupansheng.core.authority.security.entity.SysRole;
import com.pps.back.frame.pupansheng.core.authority.security.entity.SysUser;
import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysRoleDao;
import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysUserDao;
import com.pps.back.frame.pupansheng.core.authority.security.property.MySecurityProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 * @discription;
 * @time 2020/5/13 16:51
 */
@Service(value = "customUserDetailsServiceForPc")
@Slf4j
public class CustomUserDetailsServiceForPc implements UserDetailsService {

    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    SysRoleDao sysRoleDao;
    @Autowired
    MySecurityProperty mySecurityProperty;



    @Override
    public UserDetails loadUserByUsername(String username) throws ServiceException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        List<SysUser> user=new ArrayList<>();
        List<SysRole> sysRole=new ArrayList<>();
        if(mySecurityProperty.getOpenConfigUser()){

            String configUser = mySecurityProperty.getConfigUser();
            if(ValidateUtil.isEmpty(configUser)){
                log.info("开启了了本地用户配置  但是没有配置本地用户列表");
            }else {
                List<Map> maps = JSON.parseArray(configUser, Map.class);
                List<Map> userMap = maps.stream().filter(p -> p.get("username").equals(username)).collect(Collectors.toList());
                if(ValidateUtil.isNotEmpty(userMap)){
                    SysUser sysUser=new SysUser();
                    sysUser.setName((String)userMap.get(0).get("username"));
                    sysUser.setPassword((String)userMap.get(0).get("password"));
                    user.add(sysUser);
                    SysRole sysRole1=new SysRole();
                    sysRole1.setId(1L);
                    sysRole1.setName("ROLE_ADMIN");
                    sysRole.add(sysRole1);
                }

            }

        }
        SysUser sysUser=new SysUser();
        sysUser.setName(username);
        // 从数据库中取出用户信息
        if(ValidateUtil.isEmpty(user)) {
            user = sysUserDao.queryCondition(sysUser);
        }
        // 判断用户是否存在
        if(user==null||user.size()<=0) {
                throw new UsernameNotFoundException("用户不存在");
        }
        // 添加权限
        if(user.size()>1){

            throw new UsernameNotFoundException("用户存在多个 不合规范！！！！");

        }
        SysUser logUser=user.get(0);

        //获得角色信息
        if(ValidateUtil.isEmpty(sysRole)) {
            sysRole = sysRoleDao.queryRoleByUserId(logUser.getId());
        }
        //给予角色
        sysRole.stream().forEach(p->{

            authorities.add(new SimpleGrantedAuthority(p.getName()));

        });
        // 返回UserDetails实现类
        return new User(logUser.getName(), logUser.getPassword(), authorities);
    }
}
