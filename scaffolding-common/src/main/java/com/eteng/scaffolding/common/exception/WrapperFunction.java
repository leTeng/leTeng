package com.eteng.scaffolding.common.exception;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 抛出异常封装类
 * @FileName DoThrow
 * @Author eTeng
 * @Date 2020/1/5 18:38
 * @Description
 */
public class WrapperFunction {

    public static <T> Consumer<T> ThrowConsummer(Consumer<T> consumer,String message)throws Exception{
        return t -> {
            try{
                consumer.accept(t);
            }catch (Exception var1){
                throw new RuntimeException(message);
            }
        };
    }
    public static <T,R> Function<? super T, ? extends R>  ThrowFunction(Function<? super T, ? extends R> function,String message)throws Exception{
        return t -> {
            try{
                return function.apply(t);
            }catch (Exception var1){
                throw new RuntimeException(message);
            }
        };
    }
}
