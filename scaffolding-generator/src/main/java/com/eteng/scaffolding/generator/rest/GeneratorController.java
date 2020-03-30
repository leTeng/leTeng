package com.eteng.scaffolding.generator.rest;

import cn.hutool.core.util.PageUtil;
import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.generator.domain.vo.ColumnInfo;
import com.eteng.scaffolding.generator.service.GenConfigService;
import com.eteng.scaffolding.generator.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2019-01-02
 */
@RestController
@RequestMapping("/api/generator")
@Api(tags = "系统：代码生成管理")
public class GeneratorController {

    private final GeneratorService generatorService;

    private final GenConfigService genConfigService;

    @Value("${generator.enabled}")
    private Boolean generatorEnabled;

    public GeneratorController(GeneratorService generatorService, GenConfigService genConfigService) {
        this.generatorService = generatorService;
        this.genConfigService = genConfigService;
    }

    @ApiOperation("查询数据库元数据")
    @GetMapping(value = "/tables")
    public CommonResult getTables(@RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "0")Integer page,
                                  @RequestParam(defaultValue = "10")Integer size){
        int[] startEnd = PageUtil.transToStartEnd(page+1, size);
        return CommonResult.success(CommonResult.QUERY_MESSAGE,generatorService.getTables(name,startEnd));
    }

    @ApiOperation("查询表内元数据")
    @GetMapping(value = "/columns")
    public CommonResult getTables(@RequestParam String tableName){
        return CommonResult.success(CommonResult.QUERY_MESSAGE,generatorService.getColumns(tableName));
    }

    @ApiOperation("生成代码")
    @PostMapping
    public CommonResult generator(@RequestBody List<ColumnInfo> columnInfos, @RequestParam String tableName){
        if(!generatorEnabled){
            throw new RuntimeException("此环境不允许生成代码！");
        }
        generatorService.generator(columnInfos,genConfigService.find(),tableName);
        return CommonResult.success(null);
    }
}
