package com.pps.back.frame.pupansheng.core.plug.filterplug;

/**
 * @author
 * @discription;
 * @time 2020/11/11 17:46
 */
public class ConditionBuilder {
    /**
     * 若添加的sql过滤条件在原本的过滤条件之中 例如 a=#{a} + 我们的过滤语句  and b=#{b}
     *
     * @param flagString 添加的sql语句的标志字符  越准确越好  因为需要它找到插入sql语句的位置 然后在它后面或者之前 插入
     * @param sqlStart 添加的sql过滤条件的前缀语句 若为空则默认为 where
     * @param lastParamname 添加的sql过滤条件的前一个过滤参数名称
     *                      若添加的sql过滤条件在原本的过滤条件之中 例如 a=#{a} + 我们的过滤语句  and b=#{b}
     *                                                          则必须要指定过滤条件的前一个参数名称 也就是a
     */
    public static Condition createConditionAtMeddle(String flagString, String sqlStart,String lastParamname, FlagStringMode mode){

        Condition condition=new Condition();
        condition.setSqlStart(sqlStart);
        condition.setFlagString(flagString);
        condition.setFlagStringMode(mode);
        condition.setFilterSqlPostion(FilterSqlPostion.meddle);
        condition.setLastParamname(lastParamname);
        return condition;

    }

    /**
     *
     *  若添加的sql过滤条件在所有过滤条件之后 例如 a=#{a} and b=#{b} +我们的过滤语句
     *
     * @param flagString 添加的sql语句的标志字符  越准确越好  因为需要它找到插入sql语句的位置 然后在它后面或者之前 插入
     * @param sqlStart 添加的sql过滤条件的前缀语句
     *
     *
     */
    public static Condition createConditionAtEnd(String flagString, String sqlStart, FlagStringMode mode){

        Condition condition=new Condition();
        condition.setSqlStart(sqlStart);
        condition.setFlagString(flagString);
        condition.setFlagStringMode(mode);
        condition.setFilterSqlPostion(FilterSqlPostion.end);
        return condition;

    }

    /**
     *
     *  若添加的sql过滤条件在所有过滤条件之前 例如 我们的过滤语句 +  a=#{a} and b=#{b} +
     *
     * @param flagString 添加的sql语句的标志字符  越准确越好  因为需要它找到插入sql语句的位置 然后在它后面或者之前 插入
     * @param sqlStart 添加的sql过滤条件的前缀语句
     *
     *
     */
    public static Condition createConditionAtFirst(String flagString, String sqlStart, FlagStringMode mode){

        Condition condition=new Condition();
        condition.setSqlStart(sqlStart);
        condition.setFlagString(flagString);
        condition.setFlagStringMode(mode);
        condition.setFilterSqlPostion(FilterSqlPostion.first);
        return condition;

    }

}
