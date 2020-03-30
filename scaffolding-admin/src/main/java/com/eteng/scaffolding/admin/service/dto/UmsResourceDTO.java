package com.eteng.scaffolding.admin.service.dto;

import com.eteng.scaffolding.common.bean.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;


/**
* @author eTeng
* @date 2019-12-27
*/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UmsResourceDTO extends BaseDTO {

    public UmsResourceDTO() {
    }

    public UmsResourceDTO(String id, Date createTime, Date updateTime, Integer del, String uri, String method, Integer type, String description) {
        super(id,createTime,updateTime,del);
        this.uri = uri;
        this.method = method;
        this.type = type;
        this.description = description;
    }

    @ApiModelProperty(value = "资源路径",required = true)
    @NotBlank(message = "资源路径不能为空")
    @Size(max = 255,message = "资源路径的最大长度为: 255")
    private String uri;

    @ApiModelProperty(value = "资源的请求方法",required = true,notes = "(POST|GET|PUT|DELETE)")
    @NotBlank(message = "资源的请求方法不能为空")
    @Size(max = 10,message = "资源的请求方法的最大长度为: 10")
    private String method;

    @ApiModelProperty("资源类型")
    private Integer type;

    @ApiModelProperty("资源描述")
    private String description;
}