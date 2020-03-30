package com.eteng.scaffolding.controller;

import com.eteng.scaffolding.common.annotation.Log;
import com.eteng.scaffolding.common.dto.CommonPage;
import com.eteng.scaffolding.service.MmsLocalMessageService;
import com.eteng.scaffolding.service.dto.MmsLocalMessageQueryCriteria;
import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import org.springframework.data.domain.Pageable;
import com.eteng.scaffolding.common.bean.BaseDTO;
import java.util.List;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import com.eteng.scaffolding.common.dto.CommonResult;
import static com.eteng.scaffolding.common.dto.CommonPage.*;
import static com.eteng.scaffolding.common.dto.CommonResult.*;

/**
* MmsLocalMessage接口
* @author eTeng
* @date 2020-03-17
*/
@Api(tags = "MmsLocalMessage接口文档")
@RestController
@RequestMapping("/mmsLocalMessage")
public class MmsLocalMessageController {

    private final MmsLocalMessageService mmsLocalMessageService;

    public MmsLocalMessageController(MmsLocalMessageService mmsLocalMessageService) {
        this.mmsLocalMessageService = mmsLocalMessageService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('mmsLocalMessage:list')")
    public void download(HttpServletResponse response) throws IOException {
        MmsLocalMessageQueryCriteria criteria = MmsLocalMessageQueryCriteria.builder().build();
        mmsLocalMessageService.download(mmsLocalMessageService.queryAll(criteria), response);
    }

    @Log("选中导出")
    @ApiOperation("选中导出")
    @PostMapping(value = "/download")
    @PreAuthorize("@el.check('umsUser:list')")
    public void download(HttpServletResponse response, @RequestBody List<MmsLocalMessageDTO> selects) throws IOException {
        mmsLocalMessageService.download(selects, response);
    }

    @GetMapping
    @Log("查询列表MmsLocalMessage")
    @ApiOperation("查询列表")
    @PreAuthorize("@el.check('mmsLocalMessage:list')")
    public CommonResult<CommonPage<MmsLocalMessageDTO>> list(@PageableDefault(sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable){
        MmsLocalMessageQueryCriteria criteria = MmsLocalMessageQueryCriteria.builder().build();
        return success(QUERY_MESSAGE,restPage(mmsLocalMessageService.queryAll(criteria,pageable)));
    }

    @GetMapping("/{id}")
    @Log("根据id查询MmsLocalMessage")
    @ApiOperation("根据id查询")
    @PreAuthorize("@el.check('mmsLocalMessage:detail')")
    public CommonResult<MmsLocalMessageDTO> one(@PathVariable String id){
        return success(QUERY_MESSAGE,mmsLocalMessageService.findById(id));
    }

    @PostMapping
    @Log("新增MmsLocalMessage")
    @ApiOperation("新增")
    @PreAuthorize("@el.check('mmsLocalMessage:add')")
    public CommonResult<MmsLocalMessageDTO> create(@Validated(BaseDTO.C.class) @RequestBody MmsLocalMessageDTO mmsLocalMessageDTO){
        return success(ADD_MESSAGE,mmsLocalMessageService.create(mmsLocalMessageDTO));
    }

    @PutMapping
    @Log("修改MmsLocalMessage")
    @ApiOperation("修改")
    @PreAuthorize("@el.check('mmsLocalMessage:edit')")
    public CommonResult<String> update(@Validated(BaseDTO.Ru.class) @RequestBody MmsLocalMessageDTO mmsLocalMessageDTO){
        mmsLocalMessageService.update(mmsLocalMessageDTO);
        return success(UPDATE_MESSAGE);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除MmsLocalMessage")
    @ApiOperation("删除")
    @PreAuthorize("@el.check('mmsLocalMessage:del')")
    public CommonResult<String> delete(@PathVariable String id){
        mmsLocalMessageService.delete(id);
        return success(DEL_MESSAGE);
    }
}