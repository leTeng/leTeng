package com.eteng.scaffolding.generator.domain.bo;

import lombok.Data;

/**
 * @FileName ColumnExtra
 * @Author eTeng
 * @Date 2019/12/24 14:53
 * @Description 数据库列扩展信息
 */
@Data
public class ColumnExtra {

    // 索引名称
    private String keyName;

    // 是否外键
    private Boolean foreignKey = false;
}
