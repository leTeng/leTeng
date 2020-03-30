package com.eteng.scaffolding.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * 一对多表单
 * @FileName OneToManyForm
 * @Author eTeng
 * @Date 2020/1/2 8:54
 * @Description
 */
@Data
@ApiModel
public class OneToManyForm {
    /**
     * 一方的id
     */
    @ApiModelProperty("绑定关系中一方的id")
    @NotBlank(message = "绑定的id不能为空")
    private String one;
    /**
     * 多方的id集合
     */
    @ApiModelProperty("绑定关系中多方方的id列表")
    private Collection<String> many = new HashSet<>();
}
