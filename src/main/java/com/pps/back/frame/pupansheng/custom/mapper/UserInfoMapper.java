package com.pps.back.frame.pupansheng.custom.mapper;

import com.pps.back.frame.pupansheng.custom.entity.UserInfoPo;
import java.util.List;

/**
* (UserInfo)表数据库访问层
*
* @author default
* @since 2021-01-05 11:28:32
*/
public interface UserInfoMapper {

/**
* 通过ID查询单条数据
*
* @param id 主键
* @return 实例对象
*/
UserInfoPo queryById(Integer id);

/**
* 通过实体作为筛选条件查询
*
* @param userInfo 实例对象
* @return 对象列表
*/
List<UserInfoPo> queryAll(UserInfoPo userInfo);



/**
* 新增数据
*
* @param userInfo 实例对象
* @return 影响行数
*/
int insert(UserInfoPo userInfo);

/**
* 修改数据
*
* @param userInfo 实例对象
* @return 影响行数
*/
int update(UserInfoPo userInfo);

/**
* 通过主键删除数据
*
* @param id 主键
* @return 影响行数
*/
int deleteById(Integer id);



/**
* 通过主键删除数据 逻辑删除
*
* @param id 主键
* @return 影响行数
*/
int deleteByIdWithFalse(Integer id);

}