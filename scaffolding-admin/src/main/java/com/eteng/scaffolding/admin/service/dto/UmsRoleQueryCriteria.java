package com.eteng.scaffolding.admin.service.dto;

import lombok.Data;
import lombok.Builder;
import com.eteng.scaffolding.common.annotation.Query;

import java.util.Collection;

/**
* @author eTeng
* @date 2019-12-27
*/
@Data
@Builder
public class UmsRoleQueryCriteria{

    @Query(propName = "id",type = Query.Type.IN)
    private Collection<String> ids;
}