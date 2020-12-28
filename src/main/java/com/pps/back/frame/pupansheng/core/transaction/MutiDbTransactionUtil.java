package com.pps.back.frame.pupansheng.core.transaction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author
 * @discription;
 * @time 2020/11/5 9:47
 */
public class MutiDbTransactionUtil {

    private static final ThreadLocal<Integer> transactionStartFlag=ThreadLocal.withInitial(()->0);

    public static void enterMutlTransaction(){
        Integer integer = transactionStartFlag.get();
        transactionStartFlag.set(++integer);
    }

    public static void releaseMutlTransaction(){
        Integer integer = transactionStartFlag.get();
        transactionStartFlag.set(--integer);
    }

    public static boolean isFirstEnterMethod(){
        Integer integer = transactionStartFlag.get();
        return  integer==0;
    };

    public static void reset() {
        transactionStartFlag.remove();
    }

    public static String keyGenarate(Method method){

        String name = method.getName();
        String collect = Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining());
        return  name+collect;

    }
}
