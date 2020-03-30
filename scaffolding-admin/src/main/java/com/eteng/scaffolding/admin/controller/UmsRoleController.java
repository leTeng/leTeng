package com.eteng.scaffolding.admin.controller;

import com.eteng.scaffolding.admin.service.UmsRoleMenuService;
import com.eteng.scaffolding.admin.service.UmsRoleResourceService;
import com.eteng.scaffolding.admin.service.UmsRoleService;
import com.eteng.scaffolding.admin.service.UmsUserRoleService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleQueryCriteria;
import com.eteng.scaffolding.common.annotation.Log;
import com.eteng.scaffolding.common.bean.BaseDTO;
import com.eteng.scaffolding.common.dto.CommonPage;
import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
* UmsRole接口
* @author eTeng
* @date 2019-12-27
*/
@Api(tags = "角色管理接口文档")
@RestController
@RequestMapping("/umsRole")
public class UmsRoleController {

    private final UmsRoleService umsRoleService;
    @Autowired
    private UmsUserRoleService umsUserRoleService;
    @Autowired
    private UmsRoleMenuService umsRoleMenuService;
    @Autowired
    private UmsRoleResourceService umsRoleResourceService;

    public UmsRoleController(UmsRoleService umsRoleService) {
        this.umsRoleService = umsRoleService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('umsRole:list')")
    public void download(HttpServletResponse response) throws IOException {
        UmsRoleQueryCriteria criteria = UmsRoleQueryCriteria.builder().build();
        umsRoleService.download(umsRoleService.queryAll(criteria), response);
    }

    @Log("选中导出")
    @ApiOperation("选中导出")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('umsUser:list')")
    public void download(HttpServletResponse response, @RequestBody List<UmsRoleDTO> selects) throws IOException {
        umsRoleService.download(selects, response);
    }

    @GetMapping
    @Log("查询列表UmsRole")
    @ApiOperation("查询列表")
    @PreAuthorize("@el.check('umsRole:list')")
    public CommonResult<CommonPage<UmsRoleDTO>> list(@PageableDefault(sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
        UmsRoleQueryCriteria criteria = UmsRoleQueryCriteria.builder().build();
        return success(QUERY_MESSAGE,CommonPage.restPage(umsRoleService.queryAll(criteria,pageable)));
    }

    @GetMapping("/{id}")
    @Log("根据id查询UmsRole")
    @ApiOperation("根据id查询")
    @PreAuthorize("@el.check('umsRole:detail')")
    public CommonResult<UmsRoleDTO> one(@PathVariable String id){
        return success(QUERY_MESSAGE,umsRoleService.findById(id));
    }

    @PostMapping
    @Log("新增UmsRole")
    @ApiOperation("新增")
    @PreAuthorize("@el.check('umsRole:add')")
    public CommonResult<UmsRoleDTO> create(@Validated(BaseDTO.C.class) @RequestBody UmsRoleDTO umsRoleDTO){
        return success(ADD_MESSAGE,umsRoleService.create(umsRoleDTO));
    }

    @PutMapping
    @Log("修改UmsRole")
    @ApiOperation("修改")
    @PreAuthorize("@el.check('umsRole:edit')")
    public CommonResult<String> update(@Validated(BaseDTO.Ru.class) @RequestBody UmsRoleDTO umsRoleDTO){
        umsRoleService.update(umsRoleDTO);
        return success(UPDATE_MESSAGE);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除UmsRole")
    @ApiOperation("删除")
    @PreAuthorize("@el.check('umsRole:del')")
    public CommonResult<String> delete(@PathVariable String id){
        umsRoleService.delete(id);
        return success(DEL_MESSAGE);
    }

    @PostMapping("/user/binding")
    @Log("角色绑定用户")
    @ApiOperation("绑定用户")
    public CommonResult<String> bindingUser(@Validated @RequestBody OneToManyForm oneToManyForm){
        umsUserRoleService.bindingUser(oneToManyForm);
        return success("绑定成功");
    }

    @PostMapping("/menu/binding")
    @Log("角色绑定菜单")
    @ApiOperation("绑定菜单")
    public CommonResult<String> bindingMenu(@Validated @RequestBody OneToManyForm oneToManyForm){
        umsRoleMenuService.bindingMenu(oneToManyForm);
        return success("绑定成功");
    }

    @PostMapping("/resource/binding")
    @Log("角色绑定资源")
    @ApiOperation("绑定资源")
    public CommonResult<String> bindingResource(@Validated @RequestBody OneToManyForm oneToManyForm){
        umsRoleResourceService.bindingResource(oneToManyForm);
        return success("绑定成功");
    }
}