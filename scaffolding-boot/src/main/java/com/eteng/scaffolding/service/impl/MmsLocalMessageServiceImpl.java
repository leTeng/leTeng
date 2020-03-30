package com.eteng.scaffolding.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.common.transaction.RoutingProperties;
import com.eteng.scaffolding.common.util.SpringContextHolder;
import com.eteng.scaffolding.pojo.MmsLocalMessage;
import com.eteng.scaffolding.common.util.ValidationUtil;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.repository.MmsLocalMessageRepository;
import com.eteng.scaffolding.service.MmsLocalMessageService;
import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import com.eteng.scaffolding.service.dto.MmsLocalMessageQueryCriteria;
import com.eteng.scaffolding.service.mapper.MmsLocalMessageMapper;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eteng.scaffolding.common.util.QueryHelp;

import java.util.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.servlet.http.HttpServletResponse;

/**
* @author eTeng
* @date 2020-03-17
*/
@Service
@Slf4j
@CacheConfig(cacheNames = "mmsLocalMessage")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MmsLocalMessageServiceImpl implements MmsLocalMessageService {

    private final MmsLocalMessageRepository mmsLocalMessageRepository;

    private final MmsLocalMessageMapper mmsLocalMessageMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public MmsLocalMessageServiceImpl(MmsLocalMessageRepository mmsLocalMessageRepository, MmsLocalMessageMapper mmsLocalMessageMapper) {
        this.mmsLocalMessageRepository = mmsLocalMessageRepository;
        this.mmsLocalMessageMapper = mmsLocalMessageMapper;
    }

    @Override
    @Cacheable
    public Page<MmsLocalMessageDTO> queryAll(MmsLocalMessageQueryCriteria criteria, Pageable pageable){
        Page<MmsLocalMessage> page = mmsLocalMessageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(mmsLocalMessageMapper::toDto);
    }

    @Override
    @Cacheable
    public List<MmsLocalMessageDTO> queryAll(MmsLocalMessageQueryCriteria criteria){
        return mmsLocalMessageMapper.toDto(mmsLocalMessageRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public MmsLocalMessageDTO findById(String id) {
        MmsLocalMessage mmsLocalMessage = mmsLocalMessageRepository.findById(id).orElseGet(MmsLocalMessage::new);
        ValidationUtil.isNull(mmsLocalMessage.getId(),"MmsLocalMessage","id",id);
        return mmsLocalMessageMapper.toDto(mmsLocalMessage);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public MmsLocalMessageDTO create(MmsLocalMessageDTO resources) {
        MmsLocalMessage entity = mmsLocalMessageMapper.toEntity(resources);
        return mmsLocalMessageMapper.toDto(mmsLocalMessageRepository.save(entity));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void update(MmsLocalMessageDTO resources) {
        MmsLocalMessage mmsLocalMessage = mmsLocalMessageRepository.findById(resources.getId()).orElseGet(MmsLocalMessage::new);
        ValidationUtil.isNull( mmsLocalMessage.getId(),"MmsLocalMessage","id",resources.getId());
        MmsLocalMessage entity = mmsLocalMessageMapper.toEntity(resources);
        mmsLocalMessage.copy(entity);
        mmsLocalMessageRepository.save(mmsLocalMessage);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void delete(String id) {
        mmsLocalMessageRepository.deleteById(id);
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<MmsLocalMessageDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MmsLocalMessageDTO mmsLocalMessage : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("更新时间", mmsLocalMessage.getUpdateTime());
            map.put("创建时间", mmsLocalMessage.getCreateTime());
            map.put("是否删除", mmsLocalMessage.getDel());
            map.put("消息id", mmsLocalMessage.getMessageId());
            map.put("消息体", mmsLocalMessage.getBody());
            map.put("发送状态(0 未发送，1 发送中，2 重试，3 完成)", mmsLocalMessage.getSendStatus());
            map.put("重试次数", mmsLocalMessage.getRetry());
            map.put("最大的重试次数", mmsLocalMessage.getMaxRetry());
            map.put("版本", mmsLocalMessage.getVersion());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void sendMessage() {
        // 获取需要发送的消息(未发送和重试消息)
        MmsLocalMessageServiceImpl bean = SpringContextHolder.getBean(this.getClass());
        Collection<MmsLocalMessage> mmsLocalMessages = bean.getNeedSendMessage(CollUtil.newArrayList(MmsLocalMessageDTO.UNSEND, MmsLocalMessageDTO.RETRY));
        mmsLocalMessages.forEach(message -> {
            Integer sendStatus = message.getSendStatus();
            // 判断是新发送还是重试
            if(sendStatus == MmsLocalMessageDTO.UNSEND){
                // 新发送的修改发送状态
                try{
                    bean.sendMessage(message);
                }catch (ObjectOptimisticLockingFailureException var1){
                    versionConflict(message);
                }
            }else if(sendStatus == MmsLocalMessageDTO.RETRY){
                if (message.getRetry() == null || message.getMaxRetry() == null) {
                    log.error("重试发送的消息没有设置最大重试次数或者没有记录重试次数。消息的id为：{}",message.getMessageId());
                    return;
                }
                // 如果是重试发送，判断是否超出最大重试次数
                if(message.getRetry() >= message.getMaxRetry()){
                    // 超出最大重试次数，报警
                    message.setSendStatus(MmsLocalMessageDTO.COMPLETE);
                    mmsLocalMessageRepository.save(message);
                    log.error("重试发送消息次数超过设定的最大值，重试最大值为：{},消息id为：{}",message.getMaxRetry(),message.getMessageId());
                    return;
                }
                // 进行消息重试发送
                message.setRetry(message.getRetry() + 1);
                try{
                    bean.sendMessage(message);
                }catch (ObjectOptimisticLockingFailureException var1){
                    versionConflict(message);
                }
            }
        });
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public Collection<MmsLocalMessage> getNeedSendMessage(Collection<Integer> status){
        return mmsLocalMessageRepository.getBySendStatusIn(status);
    }

    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager",isolation = Isolation.READ_COMMITTED)
    public void sendMessage(MmsLocalMessage mmsLocalMessage){
        rabbitTemplate.convertAndSend(mmsLocalMessage.getExchange(),mmsLocalMessage.getRoutingKey(),
                mmsLocalMessage.getBody(),new CorrelationData(mmsLocalMessage.getMessageId()));
        /**
         * 发送完成后，修改状态。修改状态的时候使用乐观锁控制。因为在修改状态提交之前，消息已经被ACK。
         * 最新的状态变为了已发送或者重试状态。如果再提交当前修改的状态，会导致消息的状态倒置。会发送
         * 生循环发送消息。
         */
        Integer sendStatus = mmsLocalMessage.getSendStatus();
        if(!sendStatus.equals(MmsLocalMessageDTO.RETRY) && sendStatus.equals(MmsLocalMessageDTO.COMPLETE)){
            // 设置为发送中
            mmsLocalMessage.setSendStatus(MmsLocalMessageDTO.SENDING);
        }
        // 更新
        mmsLocalMessageRepository.save(mmsLocalMessage);
    }

    /**
     * 解决版本冲突
     * @param mmsLocalMessage
     */
    private void versionConflict(MmsLocalMessage mmsLocalMessage){
        // 版本出现冲突
        log.info("发送消息完成后，更改状态为：正在发送，出现数据版本冲突，消息id为：{}",mmsLocalMessage.getMessageId());
        MmsLocalMessage localMessage = mmsLocalMessageRepository.getByMessageId(mmsLocalMessage.getMessageId());
        Integer sendStatus = localMessage.getSendStatus();
        // 如果不是重试和完成状态，出现版本冲突后，将当前状态覆盖最新的状态
        if(!sendStatus.equals(MmsLocalMessageDTO.RETRY) && sendStatus.equals(MmsLocalMessageDTO.COMPLETE)){
            localMessage.setSendStatus(MmsLocalMessageDTO.SENDING);
            mmsLocalMessageRepository.save(localMessage);
        }
    }
}