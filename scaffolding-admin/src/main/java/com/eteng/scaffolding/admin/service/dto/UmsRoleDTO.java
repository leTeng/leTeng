package com.eteng.scaffolding.admin.service.dto;

import com.eteng.scaffolding.common.bean.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;


/**
* @author eTeng
* @date 2019-12-27
*/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UmsRoleDTO extends BaseDTO {

    public UmsRoleDTO() {
    }

    public UmsRoleDTO(String id, Date createTime, Date updateTime, Integer del, String name, String tag,String permission, String description) {
        super(id, createTime, updateTime, del);
        this.name = name;
        this.tag = tag;
        this.permission = permission;
        this.description = description;
    }

    @ApiModelProperty(value = "角色的名称(和角色的标签联合唯一)",required = true)
    @Size(max = 35,message = "(和角色的标签联合唯一)的最大长度为: 35")
    private String name;

    @ApiModelProperty("角色的标签(和角色的名称联合唯一)")
    @Size(max = 35,message = "(和角色的名称联合唯一)的最大长度为: 35")
    private String tag;

    @ApiModelProperty("角色的权限")
    @Size(max = 32,message = "角色的权限的最大长度为: 32")
    private String permission;

    @ApiModelProperty("角色的描述")
    @Size(max = 255,message = "角色的描述的最大长度为: 255")
    private String description;
}