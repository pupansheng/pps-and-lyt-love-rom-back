package com.pps.back.frame.pupansheng.core.data;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * @author
 * @discription; 用于从数据库分页获取内容
 * @time 2021/1/8 13:59
 */
public class DataList<T> implements Iterator<List<T>> {
    /**
     * 数据源头
     */
    private Object dataSource;
    private Integer dataPageSize;
    private Object [] args;
    private int count=1;
    private  int size;
    private  Method method;
    public DataList(Object mapper,String methodName,int length,Class [] paramTypes,Object ... args){
        this.method= ReflectionUtils.findMethod(mapper.getClass(), methodName,paramTypes);
        this.dataPageSize=length;
        if(args==null){
            args=new Object[]{null};
        }
        this.args=args;
        this.dataSource=mapper;
        init();
    }

    public void init(){

        PageHelper.startPage(count,dataPageSize);
        Object o =ReflectionUtils.invokeMethod(method,dataSource,args);
        Page<T> data=(Page<T>)o;
        this.size=data.getPages();

    }

    @Override
    public boolean hasNext() {
        return count<=size;
    }

    @Override
    public List<T>  next() {
        PageHelper.startPage(count++,dataPageSize);
        Object o = ReflectionUtils.invokeMethod(method, dataSource, args);
        Page<T> data=(Page<T>)o;
        return data.getResult();
    }
}
