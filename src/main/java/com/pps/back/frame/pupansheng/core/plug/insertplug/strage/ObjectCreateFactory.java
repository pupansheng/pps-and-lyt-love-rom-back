package com.pps.back.frame.pupansheng.core.plug.insertplug.strage;



import java.util.HashMap;
import java.util.Map;

/**
 * @author
 * @discription;
 * @time 2020/11/10 11:18
 */
public class ObjectCreateFactory  {
    private static final Map<Class,ObjectCreate> m=new HashMap<>();
    public static ObjectCreate getObjectCreate(Class c){
        ObjectCreate objectCreate = m.get(c);
        if(objectCreate==null){
            throw  new RuntimeException(c+":没有找到此类型的对象生成器");
        }
        return  objectCreate;
    }
   public static void put(Class c,ObjectCreate o){
        m.put(c,o);
   }
}
