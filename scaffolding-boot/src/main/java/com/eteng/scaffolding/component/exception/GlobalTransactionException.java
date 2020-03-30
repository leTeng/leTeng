package com.eteng.scaffolding.component.exception;

/**
 * 全局事务异常
 * @FileName GlobalTransactionException
 * @Author eTeng
 * @Date 2020/3/24 15:49
 * @Description
 */
public class GlobalTransactionException extends SecurityException{

    public GlobalTransactionException() {
    }

    public GlobalTransactionException(String message) {
        super(message);
    }

    public static GlobalTransactionException faild(){
        return new GlobalTransactionException();
    }

    public static GlobalTransactionException faild(String message){
        return new GlobalTransactionException(message);
    }
}
