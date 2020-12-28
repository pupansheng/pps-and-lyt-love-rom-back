package com.pps.back.frame.pupansheng.core.authority.security.mapper;

import com.pps.back.frame.pupansheng.core.authority.security.entity.SysPermisson;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface SysPermissonDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysPermisson record);

    int insertSelective(SysPermisson record);

    SysPermisson selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysPermisson record);

    int updateByPrimaryKey(SysPermisson record);

    List<SysPermisson> queryPermissonByRoleId(Long id);
}