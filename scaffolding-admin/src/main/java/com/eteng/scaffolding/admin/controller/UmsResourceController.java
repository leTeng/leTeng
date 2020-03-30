package com.eteng.scaffolding.admin.controller;

import com.eteng.scaffolding.admin.service.UmsResourceService;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsResourceQueryCriteria;
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
* UmsResource接口
* @author eTeng
* @date 2019-12-27
*/
@Api(tags = "资源管理接口文档")
@RestController
@RequestMapping("/umsResource")
public class UmsResourceController {

    private final UmsResourceService umsResourceService;

    public UmsResourceController(UmsResourceService umsResourceService) {
        this.umsResourceService = umsResourceService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umsResource:list')")
    public void download(HttpServletResponse response) throws IOException {
        UmsResourceQueryCriteria criteria = UmsResourceQueryCriteria.builder().build();
        umsResourceService.download(umsResourceService.queryAll(criteria), response);
    }

    @Log("选中导出")
    @ApiOperation("选中导出")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('umsUser:list')")
    public void download(HttpServletResponse response, @RequestBody List<UmsResourceDTO> selects) throws IOException {
        umsResourceService.download(selects, response);
    }

    @GetMapping
    @Log("查询列表UmsResource")
    @ApiOperation("查询列表")
    @PreAuthorize("@el.check('umsResource:list')")
    public CommonResult<CommonPage<UmsResourceDTO>> list(@PageableDefault(sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
        UmsResourceQueryCriteria criteria = UmsResourceQueryCriteria.builder().build();
        return success(QUERY_MESSAGE,CommonPage.restPage(umsResourceService.queryAll(criteria,pageable)));
    }

    @GetMapping("/{id}")
    @Log("根据id查询UmsResource")
    @ApiOperation("根据id查询")
    @PreAuthorize("@el.check('umsResource:detail')")
    public CommonResult<UmsResourceDTO> one(@PathVariable String id){
        return success(QUERY_MESSAGE,umsResourceService.findById(id));
    }

    @PostMapping
    @Log("新增UmsResource")
    @ApiOperation("新增")
    @PreAuthorize("@el.check('umsResource:add')")
    public CommonResult<UmsResourceDTO> create(@Validated(BaseDTO.C.class) @RequestBody UmsResourceDTO umsResourceDTO){
        return success(ADD_MESSAGE,umsResourceService.create(umsResourceDTO));
    }

    @PutMapping
    @Log("修改UmsResource")
    @ApiOperation("修改")
    @PreAuthorize("@el.check('umsResource:edit')")
    public CommonResult<String> update(@Validated(BaseDTO.Ru.class) @RequestBody UmsResourceDTO umsResourceDTO){
        umsResourceService.update(umsResourceDTO);
        return success(UPDATE_MESSAGE);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmsResource")
    @ApiOperation("删除")
    @PreAuthorize("@el.check('umsResource:del')")
    public CommonResult<String> delete(@PathVariable String id){
        umsResourceService.delete(id);
        return success(DEL_MESSAGE);
    }
}