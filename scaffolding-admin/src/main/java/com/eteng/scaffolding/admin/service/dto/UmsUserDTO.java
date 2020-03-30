package com.eteng.scaffolding.admin.service.dto;

import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.common.bean.BaseDTO;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.eteng.scaffolding.common.constant.DefaultNullDataConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;


/**
* @author eTeng
* @date 2019-12-27
*/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UmsUserDTO extends BaseDTO {

    public UmsUserDTO() {
    }

    public UmsUserDTO(String id, Date createTime, Date updateTime, Integer del, String userName, String phone, Date lastLogin, Integer type, String typeId) {
        super(id,createTime,updateTime,del);
        this.userName = userName;
        this.phone = phone;
        this.lastLogin = lastLogin;
        this.type = type;
        this.typeId = typeId;
    }

    @ApiModelProperty(value = "登录/登录名称",required = true)
    @NotBlank(message = "登录/登录名称不能为空")
    @Size(max = 35,message = "登录/登录名称的最大长度为: 35")
    private String userName;

    @ApiModelProperty("密码")
    @Size(max = 50,message = "密码的最大长度为: 50")
    private String credentials;

    @ApiModelProperty("手机号码")
    @Size(max = 12,message = "手机号码的最大长度为: 12")
    private String phone;

    @ApiModelProperty(value = "用户是否可用(1 表示可用，0表示禁用)")
    private Boolean enabled;

    @ApiModelProperty(value = "用户是否不过期(1表示不过期，0表示过期)")
    private Boolean accountNonExpired;

    @ApiModelProperty(value = "密码是否不过期(1表示不过期，0表示过期)")
    private Boolean credentialsNonExpired;

    @ApiModelProperty(value = "用户是否不锁定(1表示不锁定，0表示锁定)")
    private Boolean accountNonLocked;

    @ApiModelProperty(value = "角色Id",hidden = true)
    private UmsRoleDTO roleId;

    @ApiModelProperty(value = "最后登录时间",hidden = true)
    @JsonFormat(timezone = DTFormatConstant.ZONE_SHANGHAI,pattern = DTFormatConstant.DATE_TIME)
    private Date lastLogin = DefaultNullDataConstant.DEFALUT_CURRENT_DATE;

    @ApiModelProperty(value = "用户类型",hidden = true)
    private Integer type;

    @ApiModelProperty("用户类型id")
    @Size(max = 32,message = "用户类型id的最大长度为: 32")
    private String typeId;
}