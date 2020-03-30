package ${package}.service.dto;

import lombok.Data;
<#if hasTimestamp>
import java.sql.Timestamp;
</#if>
<#if hasDate?? && hasDate>
import java.util.Date;
</#if>
<#if hasBigDecimal?? && hasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if auto?? && !auto && pkColumnType?? && pkColumnType = 'Long'>
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
</#if>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.eteng.scaffolding.common.bean.BaseDTO;
import com.eteng.scaffolding.common.constant.DTFormatConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
* @author ${author}
* @date ${date}
*/
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${className}DTO extends BaseDTO {
<#if columns??>
    <#list columns as column>
    <#if column.columnName != 'id' && column.columnName != 'del' && column.columnName != 'create_time' && column.columnName != 'update_time'>

    <#--swagger注解生成-->
    <#if column.columnComment != ''>
    @ApiModelProperty("${column.columnComment}")
    </#if>
    <#--字段校验提示生成-->
    <#if "${column.columnComment}"??>
        <#if "${column.columnComment}"?contains('(')>
            <#assign tip = "${column.columnComment}"?substring("${column.columnComment}"?index_of("("))>
        <#else>
            <#assign tip = "${column.columnComment}">
        </#if>
    </#if>
    <#--字段校验注解生成-->
        <#if column.isNullable == 'NO' && !column.columnDefault??>
            <#if column.columnType == 'String'>
    @NotBlank(message = "${tip}不能为空")
            <#else>
    @NotNull(message = "${tip}不能为空")
            </#if>
        </#if>
        <#if column.columnType == 'String' && !column.extra.foreignKey>
    @Size(max = ${column.size},message = "${tip}的最大长度为: ${column.size}")
        </#if>
    <#if column.columnKey = 'PRI'>
    <#if !auto && pkColumnType = 'Long'>
    // 处理精度丢失问题
    @JsonSerialize(using= ToStringSerializer.class)
    </#if>
    </#if>
    <#if "${column.columnType}" == "Date">
    @JsonFormat(timezone = DTFormatConstant.ZONE_SHANGHAI,pattern = DTFormatConstant.DATE_TIME)
    </#if>
    private ${column.columnType} ${column.changeColumnName};
    </#if>
    </#list>
</#if>
}