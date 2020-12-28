package com.pps.back.frame.pupansheng.core.transaction;

import com.pps.back.frame.pupansheng.core.transaction.annotion.MutilDbTransactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author
 * @discription;
 * @time 2020/11/5 13:59
 */
public class Invocation {
    private Method method;
    private Object[] args;
    private  Object target;
    private MutilDbTransactional mutilDbTransactional;
    private Map<String,MutilDbTransactional> methodMutilDbTransactionalMap;
    public Invocation(Object t, MutilDbTransactional mutilDbTransactional,Map<String,MutilDbTransactional> methodMutilDbTransactionalMap, Method method, Object[] args) {
        target=t;
        this.method = method;
        this.args = args;
        this.mutilDbTransactional=mutilDbTransactional;
        this.methodMutilDbTransactionalMap=methodMutilDbTransactionalMap;
    }

    public MutilDbTransactional getMutilDbTransactional() {
        return mutilDbTransactional;
    }

    public Object execute() throws Throwable {
        return  method.invoke(target,args);
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public Map<String, MutilDbTransactional> getMethodMutilDbTransactionalMap() {
        return methodMutilDbTransactionalMap;
    }
}
