package ${package}.pojo;

import java.util.Date;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import com.eteng.scaffolding.common.bean.BaseEntity;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
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
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@SQLDelete(sql = "UPDATE ${tableName} SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ${tableName}) AS temp) WHERE id = ?")
@SQLDeleteAll(sql = "UPDATE ${tableName} SET del = (SELECT * FROM (SELECT MAX(del) + 1 FROM ${tableName}) AS temp) WHERE id = ?")
@Where(clause = "del = 0")
@Table(name="${tableName}"<#if index?? && (index?size > 0) || unique?? && (unique?size > 0)>,<#assign enable = true><#else>)</#if>
    <#assign x = 0>
    <#assign y = 0>
    <#if unique?? && (unique?size > 0)>
    <#assign uniCount = unique?size-1>
    <#list unique as u>
    <#if y == 0>
     <#--唯一键生成-->
     uniqueConstraints = {
     <#assign y = y + 1>
    </#if>
        <#assign uni_column = "">
        <#list u.indexColumn as column>
            <#if column_has_next>
                <#assign uni_column = "${uni_column}" + "\"${column.columnName}\"" + ",">
            <#else>
                <#assign uni_column = "${uni_column}" + "\"${column.columnName}\"">
            </#if>
        </#list>
        @UniqueConstraint(name = "${u.indexName}",columnNames = {${uni_column}})<#if u_index != uniCount>,<#else>}</#if>
        <#assign uni_column = "">
    </#list>
    </#if>
    <#if index?? && (index?size > 0)>
     <#assign indexCount = index?size-1>
     <#list index as idx>
     <#if x == 0 && y == 0>
     <#--索引生成-->
     indexes = {
     <#assign x = x + 1>
     <#elseif x==0 && (y > 0)>
     ,indexes = {
     <#assign x = x + 1>
     </#if>
        <#assign idx_column = "">
        <#list idx.indexColumn as column>
            <#if column_has_next>
                <#assign idx_column = "${idx_column}" + "${column.columnName}" + ",">
            <#else>
                <#assign idx_column = "${idx_column}" + "${column.columnName}">
            </#if>
        </#list>
        @Index(name = "${idx.indexName}",columnList = "${idx_column}")<#if idx_index == indexCount>}<#else>,</#if>
        <#assign idx_column = "">
    </#list>
    </#if>
<#if enable?? && enable>)</#if>
public class ${className} extends BaseEntity {
<#if columns??>
    <#list columns as column>
    <#if column.columnName != 'id' && column.columnName != 'del' && column.columnName != 'create_time' && column.columnName != 'update_time'>

    <#--字段注释-->
    <#if column.columnComment != ''>
    /**
     * ${column.columnComment}
     <#--<#if column.extra.keyName ?? && column.extra.foreignKey>
     * @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
     * @JoinTable(name = "relation_table", joinColumns = {@JoinColumn(name = "master_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "slave_id",referencedColumnName = "id")})
     * private Set<SlaveObject> slaves;
     </#if> -->
     */
    </#if>
    <#--字段外键注解-->
    <#if column.extra.keyName ?? && column.extra.foreignKey>
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "${column.columnName}")
    <#else >
    @Column(name = "${column.columnName}"<#if column.isNullable = 'NO' && column.columnKey != 'PRI'>,nullable = false</#if>)
    </#if>
    <#--字段默认值生成-->
    <#assign defaultVal = "___">
    <#if "${column.columnType}" == "Boolean">
        <#if column.columnDefault ?? && column.columnDefault?contains("0")>
            <#assign defaultVal = "false">
            <#else>
            <#assign defaultVal = "true">
        </#if>
        <#elseif "${column.columnType}" == "Date">
    <#--时间类型，转换格式-->
    @JsonFormat(timezone = DTFormatConstant.ZONE_SHANGHAI,pattern = DTFormatConstant.DATE_TIME)
            <#assign defaultVal = "new Date()">
        <#elseif "${column.columnType}" == "String">
            <#if column.columnDefault??>
                <#assign defaultVal = "\"${column.columnDefault}\"">
            </#if>
        <#else>
            <#if column.columnDefault??>
                <#assign defaultVal = "${column.columnDefault}">
            </#if>
    </#if>
    <#--字段生成-->
    private ${column.columnType} ${column.changeColumnName} <#if defaultVal != "___"> = ${defaultVal} <#assign defaultVal = "___"></#if>;
    </#if>
    </#list>
</#if>

    public void copy(${className} source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}