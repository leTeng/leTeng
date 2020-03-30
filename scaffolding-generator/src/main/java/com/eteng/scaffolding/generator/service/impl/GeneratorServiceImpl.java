package com.eteng.scaffolding.generator.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.eteng.scaffolding.common.dto.CommonPage;
import com.eteng.scaffolding.common.util.StringUtils;
import com.eteng.scaffolding.generator.config.GeneratorPropertiesConfig;
import com.eteng.scaffolding.generator.domain.GenConfig;
import com.eteng.scaffolding.generator.domain.bo.ColumnExtra;
import com.eteng.scaffolding.generator.domain.vo.ColumnInfo;
import com.eteng.scaffolding.generator.domain.vo.TableInfo;
import com.eteng.scaffolding.generator.service.GeneratorService;
import com.eteng.scaffolding.generator.service.dto.TableIndexInfo;
import com.eteng.scaffolding.generator.utils.ColUtil;
import com.eteng.scaffolding.generator.utils.GenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Zheng Jie
 * @date 2019-01-02
 */
@Service
@Slf4j
public class GeneratorServiceImpl implements GeneratorService {

    /**
     * 外键标识符
     */
    private static final String FOREIGN_KEY_IDENT = "_fk";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private GeneratorPropertiesConfig generatorPropertiesConfig;

    @Override
    @SuppressWarnings("all")
    public CommonPage<TableInfo> getTables(String name, int[] startEnd) {
        // 使用预编译防止sql注入
        String sql = "select table_name ,create_time , engine, table_collation, table_comment from information_schema.tables " +
                "where table_schema = (select database()) " +
                "and table_name like ? order by create_time desc";
        Query query = em.createNativeQuery(sql);
        query.setFirstResult(startEnd[0]);
        query.setMaxResults(startEnd[1]-startEnd[0]);
        query.setParameter(1, StringUtils.isNotBlank(name) ? ("%" + name + "%") : "%%");
        List result = query.getResultList();
        List<TableInfo> tableInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            tableInfos.add(new TableInfo(arr[0],arr[1],arr[2],arr[3], ObjectUtil.isNotEmpty(arr[4])? arr[4] : "-"));
        }
        Query query1 = em.createNativeQuery("SELECT COUNT(*) from information_schema.tables where table_schema = (select database())");
        BigInteger totalElements = (BigInteger)query1.getSingleResult();
        return CommonPage.toPage(tableInfos,startEnd[0],startEnd[1],totalElements.longValue());
    }

    @Override
    @SuppressWarnings("all")
    public CommonPage<ColumnInfo> getColumns(String name) {
        name = StringUtils.isNotBlank(name) ? name : null;
        // 使用预编译防止sql注入
        String sql = "select column_name, is_nullable, data_type, column_comment, column_key,extra,column_default,character_maximum_length from information_schema.columns " +
                "where table_name = ? and table_schema = (select database()) order by ordinal_position";
        Query query = em.createNativeQuery(sql);
        query.setParameter(1,name);
        List result = query.getResultList();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        for (Object obj : result) {
            Object[] arr = (Object[]) obj;
            BigInteger size = (BigInteger) arr[7];
            columnInfos.add(new ColumnInfo(arr[0],arr[1],arr[2],arr[3],arr[4],arr[5],arr[6], size == null?null:size.intValue(),null,"true"));
        }
        return CommonPage.toPage(columnInfos,0,10,columnInfos.size());
    }

    @Override
    public void generator(List<ColumnInfo> columnInfos, GenConfig genConfig, String tableName) {
        if(genConfig.getId() == null){
            throw new RuntimeException("请先配置生成器");
        }
        String indexSql = "show keys from " + tableName;
        Query indexQuery = em.createNativeQuery(indexSql);
        List indexResult = indexQuery.getResultList();
        Map<String,TableIndexInfo> indexMap = MapUtil.newHashMap(10);
        for (ColumnInfo columnInfo: columnInfos) {
            ColumnExtra extra = new ColumnExtra();
            String columnName = (String) columnInfo.getColumnName();
            List<Integer> indexs = indexOf(indexResult, columnName);
            // 是否外键
            Boolean foreign_key = false;
            // 列存在外键
            if(CollUtil.isNotEmpty(indexs)){
                for (Integer i : indexs) {
                    Object[] indexArr = (Object[]) indexResult.get(i);
                    foreign_key = ((String) indexArr[2]).contains(FOREIGN_KEY_IDENT);
                    extra.setForeignKey(foreign_key);
                    extra.setKeyName(columnName);
                    if(foreign_key){
                        break;
                    }
                }
            }
            columnInfo.setExtra(extra);
        }
        indexResult.forEach(obj ->{
            Object[] arr = (Object[]) obj;
            // 索引名称
            String indexName = null;
            // 是否不是唯一键
            Boolean nonUnique = null;
            // 是否外键
            Boolean foreign_key = false;
            TableIndexInfo indexInfo;
            Long aLong = ((BigInteger)arr[1]).longValue();
            nonUnique  = aLong == 0 ? false : true;
            indexName = (String) arr[2];
            foreign_key = indexName.contains(FOREIGN_KEY_IDENT);
            if(!foreign_key && !indexName.equalsIgnoreCase("PRIMARY")){
                indexInfo = indexMap.get(indexName);
                if(indexInfo == null){
                    indexInfo = new TableIndexInfo();
                    indexInfo.setIndexName(indexName);
                    indexInfo.setUnique(!nonUnique);
                    List<TableIndexInfo.IndexColumn> columns = CollUtil.newArrayList();
                    indexInfo.setIndexColumn(columns);
                    indexMap.put(indexName,indexInfo);
                }
                TableIndexInfo.IndexColumn indexColumn = new TableIndexInfo.IndexColumn();
                indexColumn.setColumnName((String)arr[4]);
                indexColumn.setChangeColumnName(StringUtils.toCapitalizeCamelCase((String)arr[4]));
                ColumnInfo column = findColumn(columnInfos, (String) arr[4]);
                if(column != null){
                    indexColumn.setColumnType(ColUtil.cloToJava(column.getColumnType().toString()));
                }
                indexInfo.getIndexColumn().add(indexColumn);
            }
        });
        try {
            GenUtil.generatorCode(columnInfos,genConfig,indexMap.values(),tableName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void generator() {
        List<GeneratorPropertiesConfig.AdminGeneratorConfig> configs = generatorPropertiesConfig.getConfigs();
        if(CollUtil.isEmpty(configs)){
            log.error("自动代码生成失败,请检查生成代码的配置文件！");
            throw new RuntimeException("自动代码生成失败,请检查生成代码的配置文件！");
        }
        configs.forEach(config -> {
            GenConfig genConfig = config.getGenConfig();
            if(ObjectUtil.isEmpty(genConfig)){
                log.error("自动代码生成失败,请检查生成代码的配置文件！");
                throw new RuntimeException("自动代码生成失败,请检查生成代码的配置文件！");
            }
            if(StrUtil.isBlank(genConfig.getModuleName()) || StrUtil.isBlank(genConfig.getPack())){
                log.error("指定的生成目标modelName或者目标pack为空，请检查生成代码的配置文件！");
                throw new RuntimeException("指定的生成目标modelName或者目标pack为空，请检查生成代码的配置文件！");
            }
            List<String> tables = config.getTables();
            if (CollUtil.isEmpty(tables)) {
                log.warn("指定的生成表名称为空，请检查生成代码的配置文件！");
                return;
            }
            int[] startEnd = {0, 1000};
            CommonPage<TableInfo> allTables = this.getTables(null, startEnd);
            Set<Object> tableNames = allTables.getList().stream().map(tableInfo -> tableInfo.getTableName()).collect(Collectors.toSet());
            tables.stream()
                    .filter(table -> {
                        boolean contains = tableNames.contains(table);
                        if(!contains){
                            log.error("未知的表名称：{}，请检查配置",table);
                        }
                        return contains;
                    })
                    .forEach(table ->{
                        CommonPage<ColumnInfo> columnsPage = this.getColumns(table);
                        if(columnsPage != null && columnsPage.getList() != null){
                            log.info("开始自动生成表：{}",table);
                            log.info("表的列信息为：{}",columnsPage.getList());
                            log.info("生成目标model:{}",config.getGenConfig().getModuleName());
                            log.info("生成目标包:{}",config.getGenConfig().getPack());
                        }
                        this.generator(columnsPage.getList(),genConfig,table);
            });
        });
    }

    private List<Integer> indexOf(List indexs,String columnName){
        List<Integer> result =  CollUtil.newArrayList();
        if(CollUtil.isEmpty(indexs)){
            return result;
        }
        for (int i = 0; i < indexs.size(); i++) {
            Object[] indexArr = (Object[]) indexs.get(i);
            // 判断列名称是否一样
            if(indexArr[4].equals(columnName)){
                result.add(i);
            }
        }
        return result;
    }

    private ColumnInfo findColumn(List<ColumnInfo> columnInfos,String columnName){
        if(CollUtil.isEmpty(columnInfos)){
            return null;
        }
        for (ColumnInfo c : columnInfos) {
            if (c.getColumnName().equals(columnName)) {
                return c;
            }
        }
        return null;
    }
}
