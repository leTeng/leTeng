package com.eteng.scaffolding.component.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.common.transaction.ReturnMessageHandler;
import com.eteng.scaffolding.common.transaction.RoutingProperties;
import com.eteng.scaffolding.pojo.MmsLocalMessage;
import com.eteng.scaffolding.repository.MmsLocalMessageRepository;
import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import com.eteng.scaffolding.service.mapper.MmsLocalMessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 重发回退的消息
 * @FileName ResendMessageHandler
 * @Author eTeng
 * @Date 2020/3/13 17:15
 * @Description
 */
@Slf4j
@Component
public class ResendMessageHandler implements ReturnMessageHandler {

    @Autowired
    private RoutingProperties routingProperties;

    @Autowired
    private MmsLocalMessageRepository mmsLocalMessageRepository;

    @Autowired
    private LostRecorder lostRecorder;

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void handler(String exchange, String routingKey, Message message) {
        // 发序列化消息信息
        try{
            MessageProperties messageProperties = message.getMessageProperties();
            Map<String, Object> headers = messageProperties.getHeaders();
            String messageId = (String) headers.get("spring_returned_message_correlation");
            if (messageId != null) {
//                // 根据id修改为未发送
//                MmsLocalMessage byMessageId = mmsLocalMessageRepository.getByMessageId(messageId);
//                if (ObjectUtil.isNull(byMessageId)) {
//                    log.error("发送消息重试，根据消息id查询消息未找到。消息id为：{}",messageId);
//                    return;
//                }
//                // 设置完成状态
//                byMessageId.setSendStatus(MmsLocalMessageDTO.RETRY);
//                // 重新发送消息
//                mmsLocalMessageRepository.save(byMessageId);
                // 记录消息回退
                lostRecorder.record(messageId);
                log.info("消息出现回退，进行重新发送消息。重新发送消息id为：{}",messageId);
            }
        }catch (Exception var1){
            log.error("消息出现回退，进行重新发送消息异常。方法名称为：handler(String exchange, String routingKey, Message message)" +
                    ",异常信息为：{}",var1.getMessage());
        }
    }

    @Override
    public <T> boolean support(String exchange, String routingKey) {
        return exchange.equals(routingProperties.getExchange());
    }
}
