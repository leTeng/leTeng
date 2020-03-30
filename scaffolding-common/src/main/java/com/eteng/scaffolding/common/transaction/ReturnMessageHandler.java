package com.eteng.scaffolding.common.transaction;

import org.springframework.amqp.core.Message;

/**
 * 回退消息处理器
 * @FileName ConfirmMessageHandler
 * @Author eTeng
 * @Date 2020/3/13 14:56
 * @Description
 */
public interface ReturnMessageHandler {

    void handler(String exchange, String routingKey, Message message);

    <T> boolean support(String exchange,String routingKey);
}