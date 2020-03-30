package ${package}.pojo;

import lombok.Data;
import java.util.Date;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import BaseEntity;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>

/**
* @author ${author}
* @date ${date}
*/
@Entity
@Data
@Table(name="${tableName}",<#if !index?? || ${index?size} == 0>)
    <#else>
        indexes = {
        <#list index as idx>
            <#if idx.unique></#if>
            @Index(name = "${idx.indexName}",columnList = ${idx.coluneName?join(",")},unique = <#if idx.unique>true<#else>false</#if>)
        </#list>
        })
</#if>
public class ${className} extends BaseEntity {
<#if columns??>
    <#list columns as column>
    <#if column.columnName != 'id' && column.columnName != 'del' && column.columnName != 'create_time' && column.columnName != 'update_time'>

    <#if column.columnComment != ''>
    /**
     * ${column.columnComment}
     */
    </#if>
    <#if column.keyName ?? && column.foreignKey>
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "${column.columnName}")
    </#if>
    @Column(name = "${column.columnName}"<#if column.isNullable = 'NO' && column.columnKey != 'PRI'>,nullable = false</#if>)
    private ${column.columnType} ${column.changeColumnName};
    </#if>
    </#list>
</#if>

    public void copy(${className} source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}