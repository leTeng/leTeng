package com.eteng.scaffolding.common.transaction;

import org.springframework.amqp.core.Message;

/**
 * @FileName ConfirmMessageHandler
 * @Author eTeng
 * @Date 2020/3/13 14:56
 * @Description
 */
public interface ConfirmMessageHandler {

    void handler(String id,boolean ack);

    <T> boolean support(String id);
}