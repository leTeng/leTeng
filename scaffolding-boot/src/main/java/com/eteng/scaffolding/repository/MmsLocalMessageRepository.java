package com.eteng.scaffolding.repository;

import com.eteng.scaffolding.pojo.MmsLocalMessage;
import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

/**
* @author eTeng
* @date 2020-03-17
*/
public interface MmsLocalMessageRepository extends JpaRepository<MmsLocalMessage, String>, JpaSpecificationExecutor<MmsLocalMessage> {

    /**
     * 消息id查询消息
     * @param messageId 消息id
     * @return
     */
    MmsLocalMessage getByMessageId(String messageId);

    /**
     * 通过多发送状态查询本地消息
     * @param status
     * @return
     */
    Collection<MmsLocalMessage> getBySendStatusIn(Collection<Integer> status);
}