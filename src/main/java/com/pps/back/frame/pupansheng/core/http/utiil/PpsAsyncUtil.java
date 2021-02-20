package com.pps.back.frame.pupansheng.core.http.utiil;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author
 * @discription;
 * @time 2021/1/15 15:06
 */
public class PpsAsyncUtil {


    public static <T> void submit(Supplier<T> supplier, Consumer<T> consumer){
        CompletableFuture.supplyAsync(supplier).thenAccept(consumer);
    }
    public static <T> void submit(Supplier<T> supplier, Consumer<T> consumer,Consumer<Throwable> exh){
        CompletableFuture.supplyAsync(supplier).thenAccept(consumer).exceptionally((ex)->{
            exh.accept(ex);
            return null;
        });
    }

}
