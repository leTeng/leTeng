package com.eteng.scaffolding.admin.service.dto;

import com.eteng.scaffolding.common.bean.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
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
@ApiModel
public class UmsMenuDTO extends BaseDTO {

    public UmsMenuDTO() {
    }

    public UmsMenuDTO(String id, Date createTime, Date updateTime, Integer del, String name, String uri, Boolean enable, String parentId, Integer sort, String value, Integer type, String icon, String description) {
        super(id,createTime,updateTime,del);
        this.name = name;
        this.uri = uri;
        this.enable = enable;
        this.parentId = parentId;
        this.sort = sort;
        this.value = value;
        this.type = type;
        this.icon = icon;
        this.description = description;
    }

    public UmsMenuDTO(String id,Date createTime, Date updateTime, Integer del,  String name, String parentId) {
        super(id,createTime,updateTime,del);
        this.name = name;
        this.parentId = parentId;
    }

    @ApiModelProperty(value = "菜单名称",required = true)
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 35,message = "菜单名称的最大长度为: 35")
    private String name;

    @ApiModelProperty(value = "菜单的路径",required = true)
    @NotBlank(message = "菜单的路径不能为空")
    @Size(max = 255,message = "菜单的路径的最大长度为: 255")
    private String uri;

    @ApiModelProperty("是否显示菜单(true为显示，false为不显示。默认为显示)")
    private Boolean enable;

    @ApiModelProperty("上一级菜单")
    @Size(max = 32,message = "上一级菜单的最大长度为: 32")
    private String parentId;

    @ApiModelProperty("菜单排序")
    private Integer sort;

    @ApiModelProperty("菜单的权限值")
    @Size(max = 100,message = "菜单的权限值的最大长度为: 100")
    private String value;

    @ApiModelProperty("菜单的类型(0为目录，1为菜单，2为按钮)")
    private Integer type;

    @ApiModelProperty("菜单的图标")
    @Size(max = 255,message = "菜单的图标的最大长度为: 255")
    private String icon;

    @ApiModelProperty("菜单描述")
    @Size(max = 255,message = "菜单描述的最大长度为: 255")
    private String description;
}