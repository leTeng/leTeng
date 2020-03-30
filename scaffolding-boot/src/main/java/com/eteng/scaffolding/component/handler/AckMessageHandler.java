package com.eteng.scaffolding.component.handler;

import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.common.transaction.ConfirmMessageHandler;
import com.eteng.scaffolding.common.util.SpringContextHolder;
import com.eteng.scaffolding.pojo.MmsLocalMessage;
import com.eteng.scaffolding.repository.MmsLocalMessageRepository;
import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 消息发送成功确认处理器
 * @FileName ConfirmtransactionMessageHandler
 * @Author eTeng
 * @Date 2020/3/13 16:04
 * @Description
 */
@Slf4j
@Component
public class AckMessageHandler implements ConfirmMessageHandler {

    @Autowired
    private MmsLocalMessageRepository mmsLocalMessageRepository;

    @Autowired
    private LostRecorder lostRecorder;

    @Override
    public void handler(String id, boolean ack) {
        AckMessageHandler bean = SpringContextHolder.getBean(this.getClass());
        if (bean != null) {
            try{
                bean.ack(id,ack);
            }catch (ObjectOptimisticLockingFailureException var1){
                log.info("发送消息完成后，更改状态为：正在发送，出现数据版本冲突，消息id为：{}",id);
                bean.ack(id,ack);
            }
            if(lostRecorder.isRecord(id)){
                // 清空回退消息记录
                lostRecorder.clean(id);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager",isolation = Isolation.READ_COMMITTED)
    public void ack(String id, boolean ack){
        /*
         * 是否是ack，如果消息未找到队列。将由returnCallback处理，此时ack为true，这种情况需要处理。
         * 并且ACK消息之前一定要的等消息发送完成后(本地消息表的状态更改已提交)，才能ACK消息
         */
        if (ack && !lostRecorder.isRecord(id)) {
            // 根据id修改完成状态
            ack(id);
        }else{
            unAck(id);
        }
    }

    @Override
    public <T> boolean support(String id) {
        if(id != null){
            MmsLocalMessage byMessageId = mmsLocalMessageRepository.getByMessageId(id);
            return byMessageId != null;
        }
        return false;
    }

    private void ack(String id){
        MmsLocalMessage byMessageId = mmsLocalMessageRepository.getByMessageId(id);
        if (ObjectUtil.isNull(byMessageId)) {
            log.error("发送消息确认，根据消息id查询消息未找到。消息id为：{}",id);
            return;
        }
        // 设置完成状态
        byMessageId.setSendStatus(MmsLocalMessageDTO.COMPLETE);
        mmsLocalMessageRepository.save(byMessageId);
    }

    private void unAck(String id){
        MmsLocalMessage byMessageId;
        byMessageId = mmsLocalMessageRepository.getByMessageId(id);
        // 根据id修改为未发送
        if (ObjectUtil.isNull(byMessageId)) {
            log.error("发送消息重试，根据消息id查询消息未找到。消息id为：{}",id);
            return;
        }
        // 设置重试状态
        byMessageId.setSendStatus(MmsLocalMessageDTO.RETRY);
        mmsLocalMessageRepository.save(byMessageId);
        try{
            TimeUnit.SECONDS.sleep(10);
        }catch (Exception va1){

        }
        log.info("消息ACK失败，设置消息重新发送。消息id为: {}",id);
    }
}
