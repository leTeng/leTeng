package com.eteng.scaffolding.admin.service;

import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsRoleService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<UmsRoleDTO>
    */
    Page<UmsRoleDTO> queryAll(UmsRoleQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmsRoleDTO>
    */
    List<UmsRoleDTO> queryAll(UmsRoleQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmsRoleDTO
     */
    UmsRoleDTO findById(String id);

    UmsRoleDTO create(UmsRoleDTO resources);

    void update(UmsRoleDTO resources);

    void delete(String id);

    void download(List<UmsRoleDTO> all, HttpServletResponse response) throws IOException;


}