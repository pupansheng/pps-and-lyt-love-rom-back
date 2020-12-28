package com.pps.back.frame.pupansheng.core.plug.filterplug;

/**
 * @author pps
 * @discription;用于过滤数据库的数据  一般在已有的sal语句中会增加过滤条件
 * @time 2020/10/9 10:09
 */
public class DbFilterHelper {

    private static final ThreadLocal<Condition> CONDITON=new ThreadLocal<>();

    public static void startFilter(Condition con){

        CONDITON.set(con);

    }

    public static  void clearCondition(){

        CONDITON.remove();

    }

    public static Condition getCondition(){
        return CONDITON.get();
    }

}
