package com.eteng.scaffolding.generator.rest;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.generator.domain.GenConfig;
import com.eteng.scaffolding.generator.service.GenConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zheng Jie
 * @date 2019-01-14
 */
@RestController
@RequestMapping("/api/genConfig")
@Api(tags = "系统：代码生成器配置管理")
public class GenConfigController {

    private final GenConfigService genConfigService;

    public GenConfigController(GenConfigService genConfigService) {
        this.genConfigService = genConfigService;
    }

    @ApiOperation("查询")
    @GetMapping
    public CommonResult get(){
        return CommonResult.success("查询成功",genConfigService.find());
    }

    @ApiOperation("修改")
    @PutMapping
    public CommonResult update(@Validated @RequestBody GenConfig genConfig){
        return CommonResult.success("修改成功",genConfigService.update(genConfig));
    }
}
