package com.pps.back.frame.pupansheng.core.data;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author pps
 * @discription; 用于从数据库分页获取数据内容 一般用于定时任务等需要从数据库取所有数据等场景
 *               如果全部获取内容则数据量太大会发生内存危机 所以写了此类 使用迭代器模式分次从数据库得到数据
 * @time 2021/1/8 13:59
 */
public class DataListHelper<T> implements Iterable {

    private Iterator<List<T>> iterator;

    /**
     *
     * @param mapper      mapper对象
     * @param methodName 从数据库查询的方法名
     * @param paramTypes 方法参数 若次类方法没有发生重载 则可以为空
     * @param length     每次从数据库查询的数据大小
     * @param args       查询参数
     */
    public  DataListHelper(Object mapper,String methodName,Class [] paramTypes,int length,Object ... args){
          iterator= new DataList(mapper,methodName,length,paramTypes,args);
    }

    /**
     *  使用此构造则默认mapper 没有发生重载
     * @param mapper      mapper对象
     * @param methodName 从数据库查询的方法名
     * @param length     每次从数据库查询的数据大小
     * @param args       查询参数
     */
    public  DataListHelper(Object mapper,String methodName,int length,Object ... args){
        iterator= new DataList(mapper,methodName,length,null,args);
    }

    @Override
    public Iterator<List<T>> iterator() {
        return  iterator;
    }

    @Override
    public void forEach(Consumer action) {

        while (iterator.hasNext()){
            List<T> next = iterator.next();
            next.forEach(action::accept);
        }

    }
    public void forEachT(Consumer<T> action) {

        while (iterator.hasNext()){
            List<T> next = iterator.next();
            next.forEach(action::accept);
        }

    }
}
