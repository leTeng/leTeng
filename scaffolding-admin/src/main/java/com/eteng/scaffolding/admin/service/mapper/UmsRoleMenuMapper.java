package com.eteng.scaffolding.admin.service.mapper;

import com.eteng.scaffolding.common.bean.BaseMapper;
import com.eteng.scaffolding.admin.pojo.UmsRoleMenu;
import com.eteng.scaffolding.admin.service.dto.UmsRoleMenuDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author eTeng
* @date 2019-12-27
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UmsRoleMenuMapper extends BaseMapper<UmsRoleMenuDTO, UmsRoleMenu> {

}