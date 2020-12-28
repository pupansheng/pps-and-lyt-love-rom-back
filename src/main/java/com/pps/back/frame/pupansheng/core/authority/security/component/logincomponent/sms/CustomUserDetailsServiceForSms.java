package com.pps.back.frame.pupansheng.core.authority.security.component.logincomponent.sms;

import com.pps.back.frame.pupansheng.core.authority.security.component.exception.ServiceException;
import com.pps.back.frame.pupansheng.core.authority.security.entity.SysRole;
import com.pps.back.frame.pupansheng.core.authority.security.entity.SysUser;
import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysRoleDao;
import com.pps.back.frame.pupansheng.core.authority.security.mapper.SysUserDao;
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

/**
 * @author
 * @discription;
 * @time 2020/5/16 19:16
 */
@Service(value = "customUserDetailsServiceForSms")
public class CustomUserDetailsServiceForSms  implements UserDetailsService {

    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysRoleDao sysRoleDao;


    @Override
    public UserDetails loadUserByUsername(String phone) throws ServiceException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        return new User("", "", authorities);
    }

    public UserDetails loadUserByPhone(String phone) throws ServiceException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // 从数据库中取出用户信息
        SysUser sysUser=new SysUser();
        sysUser.setPhone(phone);
        List<SysUser> user = sysUserDao.queryCondition(sysUser);
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
        List<SysRole> sysRole = sysRoleDao.queryRoleByUserId(logUser.getId());

        //给予角色
        sysRole.stream().forEach(p->{

            authorities.add(new SimpleGrantedAuthority(p.getName()));

        });
        // 返回UserDetails实现类
        return new User(logUser.getName(), logUser.getPassword(), authorities);
    }
}
