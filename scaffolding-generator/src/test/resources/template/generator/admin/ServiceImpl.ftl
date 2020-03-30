package ${package}.service.impl;

import ${package}.pojo.${className};
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.util.ValidationUtil;
import com.eteng.scaffolding.common.util.FileUtil;
import ${package}.repository.${className}Repository;
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}DTO;
import ${package}.service.dto.${className}QueryCriteria;
import ${package}.service.mapper.${className}Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
<#if !auto && pkColumnType = 'Long'>
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
</#if>
<#if !auto && pkColumnType = 'String'>
import cn.hutool.core.util.IdUtil;
</#if>
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eteng.scaffolding.common.util.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author ${author}
* @date ${date}
*/
@Service
@CacheConfig(cacheNames = "${changeClassName}")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ${className}ServiceImpl implements ${className}Service {

    private final ${className}Repository ${changeClassName}Repository;

    private final ${className}Mapper ${changeClassName}Mapper;

    public ${className}ServiceImpl(${className}Repository ${changeClassName}Repository, ${className}Mapper ${changeClassName}Mapper) {
        this.${changeClassName}Repository = ${changeClassName}Repository;
        this.${changeClassName}Mapper = ${changeClassName}Mapper;
    }

    @Override
    @Cacheable
    public Page<${className}DTO> queryAll(${className}QueryCriteria criteria, Pageable pageable){
        Page<${className}> page = ${changeClassName}Repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(${changeClassName}Mapper::toDto);
    }

    @Override
    @Cacheable
    public List<${className}DTO> queryAll(${className}QueryCriteria criteria){
        return ${changeClassName}Mapper.toDto(${changeClassName}Repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public ${className}DTO findById(${pkColumnType} ${pkChangeColName}) {
        ${className} ${changeClassName} = ${changeClassName}Repository.findById(${pkChangeColName}).orElseGet(${className}::new);
        ValidationUtil.isNull(${changeClassName}.get${pkCapitalColName}(),"${className}","${pkChangeColName}",${pkChangeColName});
        return ${changeClassName}Mapper.toDto(${changeClassName});
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public ${className}DTO create(${className}DTO resources) {
<#--<#if !auto && pkColumnType = 'Long'>
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.set${pkCapitalColName}(snowflake.nextId()); 
</#if>
<#if !auto && pkColumnType = 'String'>
        resources.set${pkCapitalColName}(IdUtil.simpleUUID()); 
</#if>-->
<#if columns??>
    <#--遍历唯一键-->
    <#if unique?? && (unique?size > 0)>
    <#list unique as idx>
        <#if (idx.indexColumn?size == 1)>
        if(${changeClassName}Repository.findBy${(idx.indexColumn[0].changeColumnName)}(resources.get${(idx.indexColumn[0].changeColumnName)}()) != null){
            throw new EntityExistException(${className}.class,"${idx.indexColumn[0].columnName}",resources.get${(idx.indexColumn[0].changeColumnName)}());
        }
        <#elseif idx.indexColumn?size == 2>
        if(${changeClassName}Repository.findBy${(idx.indexColumn[0].changeColumnName)}And${(idx.indexColumn[1].changeColumnName)} (resources.get${(idx.indexColumn[0].changeColumnName)}(),resources.get${(idx.indexColumn[1].changeColumnName)}()) != null){
            throw new EntityExistException(${className}.class,"${idx.indexColumn[0].columnName},${idx.indexColumn[1].columnName}",resources.get${(idx.indexColumn[0].changeColumnName)}(),resources.get${(idx.indexColumn[1].changeColumnName)}());
        }
        <#elseif idx.indexColumn?size == 3>
        if(${changeClassName}Repository.findBy${(idx.indexColumn[0].changeColumnName)}And${(idx.indexColumn[1].changeColumnName)}And${(idx.indexColumn[2].changeColumnName)} (resources.get${(idx.indexColumn[0].changeColumnName)}(),resources.get${(idx.indexColumn[1].changeColumnName)}(),resources.get${(idx.indexColumn[2].changeColumnName)}()) != null){
            throw new EntityExistException(${className}.class,"${idx.indexColumn[0].columnName},${idx.indexColumn[1].columnName},${idx.indexColumn[2].columnName}",resources.get${(idx.indexColumn[0].changeColumnName)}(),resources.get${(idx.indexColumn[1].changeColumnName)}(),resources.get${(idx.indexColumn[2].changeColumnName)}());
        }
        <#else>
            <#assign indexColumnNames = "">
            <#list idx.indexColumn as indexColumn>
                <#if indexColumn_has_next>
                    <#assign indexColumnNames = indexColumnNames + indexColumn.columnName + ",">
                    <#else>
                        <#assign indexColumnNames = indexColumnNames + indexColumn.columnName>
                </#if>
            </#list>
        // TODO 请自行实现多列联合唯一键的检验,联合唯一键序列为: ${indexColumnNames}
        </#if>
    </#list>
    </#if>
</#if>
        ${className} entity = ${changeClassName}Mapper.toEntity(resources);
        return ${changeClassName}Mapper.toDto(${changeClassName}Repository.save(entity));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(${className}DTO resources) {
        ${className} ${changeClassName} = ${changeClassName}Repository.findById(resources.get${pkCapitalColName}()).orElseGet(${className}::new);
        ValidationUtil.isNull( ${changeClassName}.get${pkCapitalColName}(),"${className}","id",resources.get${pkCapitalColName}());
<#if columns??>
    <#if unique?? && (unique?size > 0)>
    <#list unique as idx>
        <#if idx_index = 0>
        ${className} ${changeClassName}1 = null;
        </#if>
         <#if (idx.indexColumn?size == 1)>
         ${changeClassName}1 = ${changeClassName}Repository.findBy${idx.indexColumn[0].changeColumnName}(resources.get${idx.indexColumn[0].changeColumnName}());
         if(${changeClassName}1 != null && !${changeClassName}1.get${pkCapitalColName}().equals(${changeClassName}.get${pkCapitalColName}())){
            throw new EntityExistException(${className}.class,"${idx.indexColumn[0].columnName}",resources.get${idx.indexColumn[0].changeColumnName}());
         }
        <#elseif idx.indexColumn?size == 2>
        ${changeClassName}1 = ${changeClassName}Repository.findBy${idx.indexColumn[0].changeColumnName}And${idx.indexColumn[1].changeColumnName}(resources.get${idx.indexColumn[0].changeColumnName}(),resources.get${idx.indexColumn[1].changeColumnName}());
        if(${changeClassName}1 != null && !${changeClassName}1.get${pkCapitalColName}().equals(${changeClassName}.get${pkCapitalColName}())){
            throw new EntityExistException(${className}.class,"${idx.indexColumn[0].columnName},${idx.indexColumn[1].columnName}",resources.get${idx.indexColumn[0].changeColumnName}(),resources.get${idx.indexColumn[1].changeColumnName}());
        }
        <#elseif idx.indexColumn?size == 3>
         ${changeClassName}1 = ${changeClassName}Repository.findBy${idx.indexColumn[0].changeColumnName}And${idx.indexColumn[1].changeColumnName}And${idx.indexColumn[2].changeColumnName}(resources.get${idx.indexColumn[0].changeColumnName}(),resources.get${idx.indexColumn[1].changeColumnName}(),resources.get${idx.indexColumn[2].changeColumnName}());
         if(${changeClassName}1 != null && !${changeClassName}1.get${pkCapitalColName}().equals(${changeClassName}.get${pkCapitalColName}())){
            throw new EntityExistException(${className}.class,"${idx.indexColumn[0].columnName},${idx.indexColumn[1].columnName},${idx.indexColumn[2].columnName}",resources.get${idx.indexColumn[0].changeColumnName}(),resources.get${idx.indexColumn[1].changeColumnName}(),resources.get${idx.indexColumn[2].changeColumnName}());
         }
        </#if>
    </#list>
    </#if>
</#if>
        ${className} entity = ${changeClassName}Mapper.toEntity(resources);
        ${changeClassName}.copy(entity);
        ${changeClassName}Repository.save(${changeClassName});
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(${pkColumnType} ${pkChangeColName}) {
        ${changeClassName}Repository.deleteById(${pkChangeColName});
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<${className}DTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (${className}DTO ${changeClassName} : all) {
            Map<String,Object> map = new LinkedHashMap<>();
        <#list columns as column>
            <#if column.columnKey != 'PRI'>
            <#if column.columnComment != ''>
            map.put("${column.columnComment}", ${changeClassName}.get${column.capitalColumnName}());
            <#else>
            map.put(" ${column.changeColumnName}",  ${changeClassName}.get${column.capitalColumnName}());
            </#if>
            </#if>
        </#list>
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}