package com.eteng.scaffolding.pojo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.eteng.scaffolding.common.bean.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

/**
* @author eTeng
* @date 2020-03-17
*/
@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "UPDATE mms_local_message SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM mms_local_message) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE mms_local_message SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM mms_local_message) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="mms_local_message")

public class MmsLocalMessage extends BaseEntity {

    /**
     * 转换器名称
     */
    @Column(name = "exchange",nullable = false)
    private String exchange;

    /**
     * 路由键
     */
    @Column(name = "routing_key",nullable = false)
    private String routingKey;

    /**
     * 消息id
     */
    @Column(name = "message_id",nullable = false)
    private String messageId ;

    /**
     * 消息体
     */
    @Column(name = "body",nullable = false)
    private String body ;

    /**
     * 发送状态(0 未发送，1 发送中，2 重试，3 完成)
     */
    @Column(name = "send_status",nullable = false)
    private Integer sendStatus ;

    /**
     * 重试次数
     */
    @Column(name = "retry")
    private Integer retry ;

    /**
     * 最大的重试次数
     */
    @Column(name = "max_retry")
    private Integer maxRetry ;

    /**
     * 版本
     */
    @Version
    @Column(name = "version",nullable = false)
    private Integer version ;

    public void copy(MmsLocalMessage source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}