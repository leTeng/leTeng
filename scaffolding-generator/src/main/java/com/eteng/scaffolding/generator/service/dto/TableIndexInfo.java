package com.eteng.scaffolding.generator.service.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 数据表的索引信息
 * @FileName TableIndexInfo
 * @Author eTeng
 * @Date 2019/12/20 13:04
 * @Description
 */
@Data
public class TableIndexInfo {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 索引列
     */
    private List<IndexColumn> indexColumn;

    /**
     * 是否唯一键
     */
    private boolean unique;

    @Data
    public static class IndexColumn{
        /**
         * 列名称
         */
        private String columnName;
        /**
         * 列映射应用名称
         */
        private String changeColumnName;
        /**
         * 列类型
         */
        private String columnType;
    }
}
