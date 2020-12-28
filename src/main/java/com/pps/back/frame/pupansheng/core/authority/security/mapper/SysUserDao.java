package com.pps.back.frame.pupansheng.core.authority.security.mapper;

import com.pps.back.frame.pupansheng.core.authority.security.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface SysUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    List<SysUser> queryCondition(SysUser sysUser);
}