package ${package}.controller;

import com.eteng.scaffolding.common.annotation.Log;
import ${package}.pojo.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}QueryCriteria;
import ${package}.service.dto.${className}DTO;
import org.springframework.data.domain.Pageable;
import BaseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import CommonPage;
import CommonResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import static CommonResult.*;

/**
* ${className}接口
* @author ${author}
* @date ${date}
*/
@Api(tags = "${className}接口文档")
@RestController
@RequestMapping("/${changeClassName}")
public class ${className}Controller {

    private final ${className}Service ${changeClassName}Service;

    public ${className}Controller(${className}Service ${changeClassName}Service) {
        this.${changeClassName}Service = ${changeClassName}Service;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public void download(HttpServletResponse response) throws IOException {
        ${className}QueryCriteria criteria = ${className}QueryCriteria.builder().build();
        ${changeClassName}Service.download(${changeClassName}Service.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询列表${className}")
    @ApiOperation("查询列表")
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public CommonResult<CommonPage<${className}DTO>> list(){
        ${className}QueryCriteria criteria = ${className}QueryCriteria.builder().build();
        Pageable pageable = null;
        return success(QUERY_MESSAGE,${changeClassName}Service.queryAll(criteria,pageable));
    }

    @GetMapping("/{id}")
    @Log("根据id查询${className}")
    @ApiOperation("根据id查询")
    @PreAuthorize("@el.check('${changeClassName}:detail')")
    public CommonResult<${className}DTO> one(@PathVariable String id){
        return success(QUERY_MESSAGE,${changeClassName}Service.findById(id));
    }

    @PostMapping
    @Log("新增${className}")
    @ApiOperation("新增")
    @PreAuthorize("@el.check('${changeClassName}:add')")
    public CommonResult create(@Validated(BaseEntity.C.class) @RequestBody ${className} ${changeClassName}){
        return success(ADD_MESSAGE,${changeClassName}Service.create(${changeClassName}));
    }

    @PutMapping
    @Log("修改${className}")
    @ApiOperation("修改")
    @PreAuthorize("@el.check('${changeClassName}:edit')")
    public CommonResult update(@Validated(BaseEntity.Ru.class) @RequestBody ${className} ${changeClassName}){
        ${changeClassName}Service.update(${changeClassName});
        return success(UPDATE_MESSAGE);
    }

    @DeleteMapping(value = "/{${pkChangeColName}}")
    @Log("删除${className}")
    @ApiOperation("删除")
    @PreAuthorize("@el.check('${changeClassName}:del')")
    public CommonResult delete(@PathVariable ${pkColumnType} ${pkChangeColName}){
        ${changeClassName}Service.delete(${pkChangeColName});
        return success(DEL_MESSAGE);
    }
}