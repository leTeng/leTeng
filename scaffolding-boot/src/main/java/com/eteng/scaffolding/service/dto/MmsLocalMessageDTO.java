package com.eteng.scaffolding.service.dto;

import com.eteng.scaffolding.common.bean.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
* @author eTeng
* @date 2020-03-17
*/
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MmsLocalMessageDTO extends BaseDTO {

    public static final Integer UNSEND = 0;
    public static final Integer SENDING = 1;
    public static final Integer RETRY = 2;
    public static final Integer COMPLETE = 3;

    public static final Integer MAX_RETRY = 3;

    public MmsLocalMessageDTO() {
    }

    @ApiModelProperty("消息id")
    @NotBlank(message = "消息id不能为空")
    @Size(max = 36,message = "消息id的最大长度为: 36")
    private String messageId;

    @ApiModelProperty("消息体")
    @NotBlank(message = "消息体不能为空")
    @Size(max = 65535,message = "消息体的最大长度为: 65,535")
    private String body;

    @ApiModelProperty("转换器")
    @Column(name = "exchange",nullable = false)
    private String exchange;

    @ApiModelProperty("路由键")
    @Column(name = "routingKey",nullable = false)
    private String routingKey;

    @ApiModelProperty("发送状态(0 未发送，1 发送中，2 重试，3 完成)")
    @NotNull(message = "(0 未发送，1 发送中，2 重试，3 完成)不能为空")
    private Integer sendStatus;

    @ApiModelProperty("重试次数")
    private Integer retry;

    @ApiModelProperty("最大的重试次数")
    private Integer maxRetry;

    @ApiModelProperty("版本")
    @NotNull(message = "版本不能为空")
    private Integer version;
}