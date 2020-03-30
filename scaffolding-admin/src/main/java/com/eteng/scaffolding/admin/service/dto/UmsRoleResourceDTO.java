package com.eteng.scaffolding.admin.service.dto;

import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.common.bean.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
import java.io.Serializable;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotBlank;


/**
* @author eTeng
* @date 2019-12-27
*/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UmsRoleResourceDTO extends BaseDTO {

    @ApiModelProperty("角色id")
    private UmsRoleDTO roleId;

    @ApiModelProperty("资源id")
    private UmsResourceDTO resourceId;
}