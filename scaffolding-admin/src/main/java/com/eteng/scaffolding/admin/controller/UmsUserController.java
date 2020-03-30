package com.eteng.scaffolding.admin.controller;

import com.eteng.scaffolding.common.annotation.Log;
import com.eteng.scaffolding.admin.pojo.UmsUser;
import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.admin.service.dto.UmsUserQueryCriteria;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.common.bean.BaseDTO;
import org.springframework.data.domain.Pageable;
import com.eteng.scaffolding.common.bean.BaseEntity;
import java.util.List;
import com.eteng.scaffolding.common.dto.CommonPage;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.eteng.scaffolding.common.dto.CommonResult;
import static com.eteng.scaffolding.common.dto.CommonResult.*;
/**
* UmsUser接口
* @author eTeng
* @date 2019-12-27
*/
@Api(tags = "用户管理接口文档")
@RestController
@RequestMapping("/umsUser")
public class UmsUserController {

    private final UmsUserService umsUserService;

    public UmsUserController(UmsUserService umsUserService) {
        this.umsUserService = umsUserService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umsUser:list')")
    public void download(HttpServletResponse response) throws IOException {
        UmsUserQueryCriteria criteria = UmsUserQueryCriteria.builder().build();
        umsUserService.download(umsUserService.queryAll(criteria), response);
    }

    @Log("选中导出")
    @ApiOperation("选中导出")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('umsUser:list')")
    public void download(HttpServletResponse response, @RequestBody List<UmsUserDTO> selects) throws IOException {
        umsUserService.download(selects, response);
    }

    @GetMapping
    @Log("查询列表UmsUser")
    @ApiOperation("查询列表")
    @PreAuthorize("@el.check('umsUser:list')")
    public CommonResult<CommonPage<UmsUserDTO>> list(@PageableDefault(sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
        UmsUserQueryCriteria criteria = UmsUserQueryCriteria.builder().build();
        return success(QUERY_MESSAGE,CommonPage.restPage(umsUserService.queryAll(criteria,pageable)));
    }

    @GetMapping("/{id}")
    @Log("根据id查询UmsUser")
    @ApiOperation("根据id查询")
    @PreAuthorize("@el.check('umsUser:detail')")
    public CommonResult<UmsUserDTO> one(@PathVariable String id){
        return success(QUERY_MESSAGE,umsUserService.findById(id));
    }

    @PostMapping
    @Log("新增UmsUser")
    @ApiOperation("新增")
    @PreAuthorize("@el.check('umsUser:add')")
    public CommonResult<UmsUserDTO> create(@Validated(BaseDTO.C.class) @RequestBody UmsUserDTO umsUserDTO){
        return success(ADD_MESSAGE,umsUserService.create(umsUserDTO));
    }

    @PutMapping
    @Log("修改UmsUser")
    @ApiOperation("修改")
    @PreAuthorize("@el.check('umsUser:edit')")
    public CommonResult<String> update(@Validated(BaseDTO.Ru.class) @RequestBody UmsUserDTO umsUserDTO){
        umsUserService.update(umsUserDTO);
        return success(UPDATE_MESSAGE);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmsUser")
    @ApiOperation("删除")
    @PreAuthorize("@el.check('umsUser:del')")
    public CommonResult<String> delete(@PathVariable String id){
        umsUserService.delete(id);
        return success(DEL_MESSAGE);
    }
}