package com.pps.back.frame.pupansheng.core.plug.filterplug;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

/**
 * @author
 * @discription;
 * @time 2020/10/9 15:39
 */
@Slf4j
public class SqlUtil {

    /**
     *
     * @param s 原本sql
     * @param suffixString 想要加的过滤条件之后的字符串
     * @param sqlStart     加的过滤条件的前缀
     * @param keys          过滤条件
     * @return
     */
    public static String convertBefore(String s,String suffixString,String sqlStart,  Set<String> keys){

        StringBuilder stringBuilder=new StringBuilder(s);
        int startIndex = stringBuilder.indexOf(suffixString);
        if(startIndex==-1){
            log.error(suffixString+": 标志字符串>>>\n在sql语句：\n "+s+" 不存在！！\n  " +
                    "无法得到标志字符串位置!!\n " +
                    "无法动态插入sql过滤语句请检查标志字符串（注意空格注意关键词大小写 或许你的sql语句已经被规范化了 不是最初自己写的sql语句 请认真检查！pageHelper查询数目会做这一步 请注意！！）"+
                    "特别注意：若是使用了pageHelper 则一次普通查询会有两次查询\n" +
                    " 第一次是查询数据数目 这条sql为pageHelper自动生成的 它会把你的sql改写 关键字全部变成大写 空格统一  =符号会加两个空格\n" +
                    " 但是第二次查询的时候又会变成你自己的sql（没有规范化）特别需要注意！所以若要使用本插件 则sql的标识字符串应该特别注意这个问题");

          return s;
        }
        String sql="";
        boolean isFirst=true;
        for (String k:keys){
            if(isFirst){
                sql+=k+"="+"? ";
                isFirst=false;
            }else {
                sql+="and "+k+"="+"? ";
            }

        }
        if(sqlStart!=null){
            sql=sqlStart+" "+sql+" ";
        }else {
            sql="where "+sql+" ";
        }

        stringBuilder.insert(startIndex,sql);
        return  stringBuilder.toString();


    }

    /**
     *
     * @param s 原本sql
     * @param suffixString 想要加的过滤条件之前的字符串
     * @param sqlStart     加的过滤条件的前缀
     * @param keys         过滤条件
     * @return
     */
    public static String convertAfter(String s,String suffixString,String sqlStart, Set<String> keys){


        StringBuilder stringBuilder=new StringBuilder(s);
        int startIndex = stringBuilder.indexOf(suffixString);
        if(startIndex==-1){

            log.error(suffixString+": 标志字符串>>>\n在sql语句：\n "+s+" 不存在！！\n  " +
                    "无法得到标志字符串位置!!\n " +
                    "无法动态插入sql过滤语句请检查标志字符串（注意空格注意关键词大小写 或许你的sql语句已经被规范化了 不是最初自己写的sql语句 请认真检查！pageHelper查询数目会做这一步 请注意！！）"+
                    "特别注意：若是使用了pageHelper 则一次普通查询会有两次查询\n" +
                    " 第一次是查询数据数目 这条sql为pageHelper自动生成的 它会把你的sql改写 关键字全部变成大写 空格统一  =符号会加两个空格\n" +
                    " 但是第二次查询的时候又会变成你自己的sql（没有规范化）特别需要注意！所以若要使用本插件 则sql的标识字符串应该特别注意这个问题");
            return s;

        }
        startIndex=startIndex+suffixString.length();
        String sql="";
        boolean isFirst=true;
        for (String k:keys){
            if(isFirst){
                sql+=k+"="+"? ";
                isFirst=false;
            }else {
                sql+=" and "+k+"="+"? ";
            }

        }
        if(sqlStart!=null){
            sql=" "+sqlStart+" "+sql+" ";
        }else {
            sql=" where "+sql+" ";
        }

        stringBuilder.insert(startIndex,sql);
        return  stringBuilder.toString();


    }


    public static String convert(String s,  Set<String> strings){


        StringBuilder stringBuilder=new StringBuilder(s);
        int order_by = containStr(stringBuilder, "order by");
        int group_by = containStr(stringBuilder, "group by");
        int limit = containStr(stringBuilder, "limit");
        boolean o = order_by != -1;
        boolean g = group_by != -1;
        boolean l = limit != -1;
        int start = g ? group_by : o ? order_by : l ? limit : -1;
        boolean first = true;
        boolean isAppend=start==-1;
        //包含where
        if(containStr(stringBuilder,"where")!=-1){

            String addStr="";
            int len=0;
            for (String k : strings) {
                addStr=format("and " + k + "=?");
                if(isAppend){
                    stringBuilder.append(format(addStr));
                }else {
                    stringBuilder.insert(start, addStr);
                }

            }

            len=addStr.length();

        }else {//不含where
            String addStr="";
            int len=0;
            for (String k : strings) {

                if (first) {
                    addStr=format("where " + k + "=?");
                    if(isAppend){
                        stringBuilder.append(format(addStr));
                    }else {
                        stringBuilder.insert(start-len, addStr);
                    }
                } else {
                    addStr=format("and " + k + "=?");
                    if(isAppend){
                        stringBuilder.append(addStr);
                    }else {
                        stringBuilder.insert(start+len, addStr);
                    }

                }
                if (first) {
                    first = false;
                }
                len=addStr.length();
            }

        }
        return stringBuilder.toString();

    }
    private static   int containStr(StringBuilder s,String ch){
        int i = s.indexOf(ch);
        if(i==-1){
            i=s.indexOf(ch.toUpperCase());
        }
        return i;
    }

    public static String format(String s){
        return " "+s+" ";
    }
}
