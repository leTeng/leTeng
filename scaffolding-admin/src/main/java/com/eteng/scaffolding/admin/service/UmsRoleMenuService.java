package com.eteng.scaffolding.admin.service;

import com.eteng.scaffolding.admin.pojo.UmsRoleMenu;
import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleMenuQueryCriteria;
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
public interface UmsRoleMenuService extends BaseService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Page<UmsRoleMenuDTO>
    */
    Page<UmsRoleMenuDTO> queryAll(UmsRoleMenuQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<UmsRoleMenuDTO>
    */
    List<UmsRoleMenuDTO> queryAll(UmsRoleMenuQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return UmsRoleMenuDTO
     */
    UmsRoleMenuDTO findById(String id);

    UmsRoleMenuDTO create(UmsRoleMenu resources);

    void update(UmsRoleMenu resources);

    void delete(String id);

    void download(List<UmsRoleMenuDTO> all, HttpServletResponse response) throws IOException;

    void bindingMenu(OneToManyForm oneToManyForm);

    /**
     * 通过角色查询已绑定的菜单
     * @param roleId
     * @return
     */
    Collection<UmsMenuDTO> findMenuByRoleId(String roleId);

    /**
     * 通过多个角色查询已绑定的菜单
     * @param roleIds
     * @return
     */
    Collection<UmsMenuDTO> findMenuByRoleId(Collection<String> roleIds);

    /**
     * 通过多个角色id查询角色菜单绑定关系数据
     * @param roleIds
     * @return
     */
    Collection<UmsRoleMenuDTO> findByRoleIds(Collection<String> roleIds);

    /**
     * 通过菜单id查询已绑定的角色
     * @param menuId 菜单id
     * @return
     */
    Collection<UmsRoleDTO> findRoleByMenuId(String menuId);
}