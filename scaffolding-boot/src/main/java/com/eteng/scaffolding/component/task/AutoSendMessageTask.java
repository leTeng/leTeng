package com.eteng.scaffolding.component.task;

import com.eteng.scaffolding.service.MmsLocalMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 定时自动发送消息任务
 * @FileName AutoSendMessageTask
 * @Author eTeng
 * @Date 2020/3/18 15:57
 * @Description
 */
@Slf4j
@Component
public class AutoSendMessageTask {

    public static ReentrantLock lock = new ReentrantLock();

    public static ReentrantLock getLock() {
        return lock;
    }

    public static void setLock(ReentrantLock lock) {
        AutoSendMessageTask.lock = lock;
    }

    @Autowired
    private MmsLocalMessageService mmsLocalMessageService;

    /**
     * 每5秒发送一次消息
     * 定时发送的消息。可能存在多任务同时执行，导致幂等性问题。在每次发送消息的时候要锁定，
     * 是为防止在消息未发送完成(消息表的状态还没更改成功)，消息已经ACK成功(消息表的状态被
     * 更改为成功)，然后成功的状态会被定时发送重置为发送中的状态。导致发送的消息其实已经ACK,
     * 由于先于定时器更改状态，导致最新的状态被定时器覆盖。使用锁控制发送消息完成后才可以
     * ACK消息。
     * 如 线程1：未发送 -> 线程1：发送中(未提交) -> 线程2：已发送(提交，由消息中间件回调，不同线程，异步) -> 线程1： 线程1：发送中(提交)
     */
    @Scheduled(initialDelay = 10 * 1000,fixedDelay = 5 * 1000)
    public void transmitter(){
        // 定时发送消息
        log.info("开始检测发送事务消息...");
        mmsLocalMessageService.sendMessage();
    }
}
