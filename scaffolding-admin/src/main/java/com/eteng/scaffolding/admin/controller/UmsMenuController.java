package com.eteng.scaffolding.admin.controller;

import com.eteng.scaffolding.admin.service.UmsMenuService;
import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsMenuQueryCriteria;
import com.eteng.scaffolding.common.annotation.Log;
import com.eteng.scaffolding.common.bean.BaseDTO;
import com.eteng.scaffolding.common.dto.CommonPage;
import com.eteng.scaffolding.common.dto.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.eteng.scaffolding.common.dto.CommonResult.*;
/**
* UmsMenu接口
* @author eTeng
* @date 2019-12-27
*/
@Api(tags = "菜单管理接口文档")
@RestController
@RequestMapping("/umsMenu")
public class UmsMenuController {

    private final UmsMenuService umsMenuService;

    public UmsMenuController(UmsMenuService umsMenuService) {
        this.umsMenuService = umsMenuService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umsMenu:list')")
    public void download(HttpServletResponse response) throws IOException {
        UmsMenuQueryCriteria criteria = UmsMenuQueryCriteria.builder().build();
        umsMenuService.download(umsMenuService.queryAll(criteria), response);
    }

    @Log("选中导出")
    @ApiOperation("选中导出")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('umsUser:list')")
    public void download(HttpServletResponse response, @RequestBody List<UmsMenuDTO> selects) throws IOException {
        umsMenuService.download(selects, response);
    }

    @GetMapping
    @Log("查询列表UmsMenu")
    @ApiOperation("查询列表")
    @PreAuthorize("@el.check('umsMenu:list')")
    public CommonResult<CommonPage<UmsMenuDTO>> list(@PageableDefault(sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
        UmsMenuQueryCriteria criteria = UmsMenuQueryCriteria.builder().build();
        return CommonResult.success(QUERY_MESSAGE,CommonPage.restPage(umsMenuService.queryAll(criteria,pageable)));
    }

    @GetMapping("/{id}")
    @Log("根据id查询UmsMenu")
    @ApiOperation("根据id查询")
    @PreAuthorize("@el.check('umsMenu:detail')")
    public CommonResult<UmsMenuDTO> one(@PathVariable String id){
        return success(QUERY_MESSAGE,umsMenuService.findById(id));
    }

    @PostMapping
    @Log("新增UmsMenu")
    @ApiOperation("新增")
    @PreAuthorize("@el.check('umsMenu:add')")
    public CommonResult<UmsMenuDTO> create(@Validated(BaseDTO.C.class) @RequestBody UmsMenuDTO umsMenuDTO){
        return success(ADD_MESSAGE,umsMenuService.create(umsMenuDTO));
    }

    @PutMapping
    @Log("修改UmsMenu")
    @ApiOperation("修改")
    @PreAuthorize("@el.check('umsMenu:edit')")
    public CommonResult<String> update(@Validated(BaseDTO.Ru.class) @RequestBody UmsMenuDTO umsMenuDTO){
        umsMenuService.update(umsMenuDTO);
        return success(UPDATE_MESSAGE);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmsMenu")
    @ApiOperation("删除")
    @PreAuthorize("@el.check('umsMenu:del')")
    public CommonResult<String> delete(@PathVariable String id){
        umsMenuService.delete(id);
        return success(DEL_MESSAGE);
    }
}