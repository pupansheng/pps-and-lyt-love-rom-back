package com.pps.back.frame.pupansheng.core.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pps
 * @discription;
 * @time 2021/1/22 10:25
 */
public class RegexUtil {

    public static String matchOne(Pattern  pattern,String input){

        Matcher matcher = pattern.matcher(input);
        if(matcher.find()){
            return matcher.group();
        }
        return "";
    }
    public static List<String> matchMany(Pattern  pattern, String input){

        List<String> list=new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()){
         list.add(matcher.group());
        }
        return list;
    }

    /**
     * 注意特殊字符
     * @param start
     * @param end
     * @param content
     * @return
     */
    public static String findOneContentByStartAndEnd(String start,String end,String content){
        String parr="(?<=%s)[\\s\\S]*?(?=%s)";
        String format = String.format(parr, start, end);
        Matcher matcher = Pattern.compile(format).matcher(content);
        if(matcher.find()){
            return  matcher.group();
        }
        return "";
    }

    /**
     * 注意特殊字符
     * @param start
     * @param end
     * @param content
     * @return
     */
    public static List<String> findManyContentByStartAndEnd(String start, String end, String content){

        List<String> list=new ArrayList<>();
        String parr="(?<=%s)[\\s\\S]*?(?=%s)";
        String format = String.format(parr, start, end);
        Matcher matcher = Pattern.compile(format).matcher(content);
        while (matcher.find()){
            String group = matcher.group();
            list.add(group);
        }
        return list;
    }
}
