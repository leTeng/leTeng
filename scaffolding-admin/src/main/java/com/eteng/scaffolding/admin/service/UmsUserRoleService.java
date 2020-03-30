package com.eteng.scaffolding.admin.service;

import com.eteng.scaffolding.admin.pojo.UmsUserRole;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserRoleQueryCriteria;
import com.eteng.scaffolding.common.bean.BaseService;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsUserRoleService extends BaseService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<UmsUserRoleDTO>
    */
    Page<UmsUserRoleDTO> queryAll(UmsUserRoleQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmsUserRoleDTO>
    */
    List<UmsUserRoleDTO> queryAll(UmsUserRoleQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmsUserRoleDTO
     */
    UmsUserRoleDTO findById(String id);

    UmsUserRoleDTO create(UmsUserRole resources);

    void update(UmsUserRole resources);

    void delete(String id);

    void download(List<UmsUserRoleDTO> all, HttpServletResponse response) throws IOException;

    /**
     * 角色绑定用户
     * @param oneToManyForm
     */
    void bindingUser(OneToManyForm oneToManyForm);

    /**
     * 通过用户查询其拥有的角色
     * @param userId 用户id
     * @return
     */
    Collection<UmsRoleDTO> getRoleByCurrUser(String userId);

    /**
     * 通过当前角色查询其拥有的用户
     * @param roleId 角色id
     * @return
     */
    Collection<UmsUserDTO> getUserByRoleId(String roleId);
}