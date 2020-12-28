package com.pps.back.frame.pupansheng.core.plug.filterplug;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;

/**
 * @author
 * @discription;
 * @time 2020/10/9 10:54
 */
@Slf4j
public class DefaultSqlResove implements SqlResove {

    @Override
    public String filterSql(Condition condition, BoundSql boundSql, Object parameter) {
            String sourceSql = boundSql.getSql();
            String convert="";
            switch (condition.getFlagStringMode()){
                case none:
                  convert=   SqlUtil.convert(sourceSql, condition.getKeys());
                    if(sourceSql.equals(convert)){//没有变化 说明出错了 那么撤销过滤条件
                        condition.getFilterData().clear();
                    }
                    break;
                case after:
                   convert=  SqlUtil.convertAfter(sourceSql,condition.getFlagString(),condition.getSqlStart(),condition.getKeys());
                   if(sourceSql.equals(convert)){//没有变化 说明出错了 那么撤销过滤条件
                      condition.getFilterData().clear();
                   }
                    break;
                case before:
                     convert=  SqlUtil.convertBefore(sourceSql,condition.getFlagString(),condition.getSqlStart(),condition.getKeys());
                    if(sourceSql.equals(convert)){//没有变化 说明出错了 那么撤销过滤条件
                        condition.getFilterData().clear();
                    }
                    break;
            }

            return  convert;


    }





}
