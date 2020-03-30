package ${package}.service.mapper;

import com.eteng.scaffolding.common.bean.BaseMapper;
import ${package}.pojo.${className};
import ${package}.service.dto.${className}DTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author ${author}
* @date ${date}
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${className}Mapper extends BaseMapper<${className}DTO, ${className}> {

}