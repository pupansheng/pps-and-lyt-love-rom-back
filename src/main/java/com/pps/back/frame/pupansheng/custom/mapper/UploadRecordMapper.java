package com.pps.back.frame.pupansheng.custom.mapper;

import com.pps.back.frame.pupansheng.custom.entity.UploadRecordPo;
import java.util.List;

/**
* (UploadRecord)表数据库访问层
*
* @author default
* @since 2020-12-30 17:46:18
*/
public interface UploadRecordMapper{

/**
* 通过ID查询单条数据
*
* @param id 主键
* @return 实例对象
*/
UploadRecordPo queryById(String id);

/**
* 通过实体作为筛选条件查询
*
* @param uploadRecord 实例对象
* @return 对象列表
*/
List<UploadRecordPo> queryAll(UploadRecordPo uploadRecord);



/**
* 新增数据
*
* @param uploadRecord 实例对象
* @return 影响行数
*/
int insert(UploadRecordPo uploadRecord);

/**
* 修改数据
*
* @param uploadRecord 实例对象
* @return 影响行数
*/
int update(UploadRecordPo uploadRecord);

/**
* 通过主键删除数据
*
* @param id 主键
* @return 影响行数
*/
int deleteById(String id);



/**
* 通过主键删除数据 逻辑删除
*
* @param id 主键
* @return 影响行数
*/
int deleteByIdWithFalse(String id);

}