package com.pps.back.frame.pupansheng.core.authority.security.mapper;

import com.pps.back.frame.pupansheng.core.authority.security.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface SysRoleDao {
    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    List<SysRole> queryRoleByUserId(Long id);
}