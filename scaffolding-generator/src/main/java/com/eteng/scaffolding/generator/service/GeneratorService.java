package com.eteng.scaffolding.generator.service;

import com.eteng.scaffolding.common.dto.CommonPage;
import com.eteng.scaffolding.generator.domain.GenConfig;
import com.eteng.scaffolding.generator.domain.vo.ColumnInfo;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2019-01-02
 */
public interface GeneratorService {

    /**
     * 查询数据库元数据
     * @param name 表名
     * @param startEnd 分页参数
     * @return /
     */
    Object getTables(String name, int[] startEnd);

    /**
     * 得到数据表的元数据
     * @param name 表名
     * @return /
     */
    CommonPage getColumns(String name);

    /**
     * 生成代码
     * @param columnInfos 表字段数据
     * @param genConfig 代码生成配置
     * @param tableName 表名
     */
    void generator(List<ColumnInfo> columnInfos, GenConfig genConfig, String tableName);

    /**
     * 根据配置生成
     */
    void generator();
}
