package com.eteng.scaffolding.admin.service;

import com.eteng.scaffolding.admin.pojo.UmsRoleResource;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleResourceQueryCriteria;
import com.eteng.scaffolding.common.bean.BaseService;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsRoleResourceService extends BaseService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<UmsRoleResourceDTO>
    */
    Page<UmsRoleResourceDTO> queryAll(UmsRoleResourceQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmsRoleResourceDTO>
    */
    List<UmsRoleResourceDTO> queryAll(UmsRoleResourceQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmsRoleResourceDTO
     */
    UmsRoleResourceDTO findById(String id);

    UmsRoleResourceDTO create(UmsRoleResource resources);

    void update(UmsRoleResource resources);

    void delete(String id);

    void download(List<UmsRoleResourceDTO> all, HttpServletResponse response) throws IOException;

    /**
     * 角色绑定资源
     * @param oneToManyForm
     */
    void bindingResource(OneToManyForm oneToManyForm);

    /**
     * 通过多个角色查询已绑定的资源
     * @param roleIds 角色id
     * @return
     */
    Collection<UmsResourceDTO> findResourceByRoleId(Collection<String> roleIds);

    /**
     * 通过多个角色id查询角色资源绑定关系数据
     * @param roleIds
     * @return
     */
    Collection<UmsRoleResourceDTO> findByRoleIds(Collection<String> roleIds);

    /**
     * 通过资源id查询角色
     * @param resourceId
     * @return
     */
    Collection<UmsRoleDTO> findRoleByResourceId(String resourceId);
}