package ${package}.repository;

import ${package}.pojo.${className};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author ${author}
* @date ${date}
*/
public interface ${className}Repository extends JpaRepository<${className}, ${pkColumnType}>, JpaSpecificationExecutor<${className}> {
<#if columns??>
    <#if unique?? && (unique?size > 0)>
    <#list unique as idx>
            <#if (idx.indexColumn?size == 1)>

    ${className} findBy${(idx.indexColumn[0].changeColumnName)}(${idx.indexColumn[0].columnType} ${(idx.indexColumn[0].changeColumnName?uncap_first)});
            <#elseif (idx.indexColumn?size == 2)>

    ${className} findBy${(idx.indexColumn[0].changeColumnName)}And${(idx.indexColumn[1].changeColumnName)} (${idx.indexColumn[0].columnType} ${(idx.indexColumn[0].changeColumnName?uncap_first)},${idx.indexColumn[1].columnType} ${(idx.indexColumn[1].changeColumnName?uncap_first)});
            <#elseif (idx.indexColumn?size == 3)>

    ${className} findBy${(idx.indexColumn[0].changeColumnName)}And${(idx.indexColumn[1].changeColumnName)}And${(idx.indexColumn[2].changeColumnName)} (${idx.indexColumn[0].columnType} ${(idx.indexColumn[0].changeColumnName?uncap_first)},${idx.indexColumn[1].columnType} ${(idx.indexColumn[1].changeColumnName?uncap_first)},${idx.indexColumn[2].columnType} ${(idx.indexColumn[2].changeColumnName?uncap_first)});
            <#else>
                <#assign indexColumnNames = "">
                <#list idx.indexColumn as indexColumn>
                    <#if indexColumn_has_next>
                        <#assign indexColumnNames = indexColumnNames + indexColumn.changeColumnName + ",">
                    <#else>
                        <#assign indexColumnNames = indexColumnNames + indexColumn.changeColumnName>
                    </#if>
                </#list>
    // TODO 请自行实现多列联合唯一键的检验,联合唯一键序列为: ${indexColumnNames}
            </#if>
    </#list>
    </#if>
</#if>
}