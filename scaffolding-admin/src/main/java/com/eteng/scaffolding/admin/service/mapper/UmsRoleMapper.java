package com.eteng.scaffolding.admin.service.mapper;

import com.eteng.scaffolding.common.bean.BaseMapper;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author eTeng
* @date 2019-12-27
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UmsRoleMapper extends BaseMapper<UmsRoleDTO, UmsRole> {

}