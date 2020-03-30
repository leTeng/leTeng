package com.eteng.scaffolding.service.mapper;

import com.eteng.scaffolding.common.bean.BaseMapper;
import com.eteng.scaffolding.pojo.MmsLocalMessage;
import com.eteng.scaffolding.service.dto.MmsLocalMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author eTeng
* @date 2020-03-17
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MmsLocalMessageMapper extends BaseMapper<MmsLocalMessageDTO, MmsLocalMessage> {

}