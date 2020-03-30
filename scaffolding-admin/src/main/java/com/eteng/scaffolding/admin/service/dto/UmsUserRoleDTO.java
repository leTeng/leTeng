package com.eteng.scaffolding.admin.service.dto;

import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsUser;
import com.eteng.scaffolding.common.bean.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
* @author eTeng
* @date 2019-12-27
*/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UmsUserRoleDTO extends BaseDTO {

    @ApiModelProperty("用户id")
    private UmsUserDTO userId;

    @ApiModelProperty("角色id")
    private UmsRoleDTO roleId;
}