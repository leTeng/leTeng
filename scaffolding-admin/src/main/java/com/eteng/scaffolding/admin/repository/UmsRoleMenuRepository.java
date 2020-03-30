package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsRoleMenu;
import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsRoleMenuRepository extends JpaRepository<UmsRoleMenu, String>, JpaSpecificationExecutor<UmsRoleMenu> {
    /**
     * 通过角色id查询菜单
     * @param roleId
     * @return
     */
    @Query("select new com.eteng.scaffolding.admin.service.dto.UmsMenuDTO(m.id,m.createTime,m.updateTime,m.del,m.name,m.uri,m.enable,m.parentId,m.sort,m.value,m.type,m.icon,m.description)" +
            "from UmsRoleMenu rm " +
            "left join rm.roleId r on rm.roleId = r.id " +
            "left join rm.menuId m on rm.menuId = m.id " +
            "where r.id = ?1")
    List<UmsMenuDTO> findMenuByRoleId(String roleId);

    /**
     * 通过多个角色id查询菜单
     * @param roleIds
     * @return
     */
    @Query("select new com.eteng.scaffolding.admin.service.dto.UmsMenuDTO(m.id,m.createTime,m.updateTime,m.del,m.name,m.uri,m.enable,m.parentId,m.sort,m.value,m.type,m.icon,m.description)" +
            "from UmsRoleMenu rm " +
            "left join rm.roleId r on rm.roleId = r.id " +
            "left join rm.menuId m on rm.menuId = m.id " +
            "where r.id IN ?1")
    List<UmsMenuDTO> findMenuByRoleIds(Collection<String> roleIds);

    /**
     * 通过多个角色id查询的角色菜单的关系数据
     * @param umsRole
     * @return
     */
    List<UmsRoleMenu> findByRoleIdIn(Collection<UmsRole> umsRole);

    /**
     * 通过菜单id查询角色
     * @param menuId
     * @return
     */
    @Query("select new com.eteng.scaffolding.admin.service.dto.UmsRoleDTO(r.id,r.createTime,r.updateTime,r.del,r.name,r.tag,r.permission,r.description)" +
            "from UmsRoleMenu rm " +
            "left join rm.roleId r on rm.roleId = r.id " +
            "left join rm.menuId m on rm.menuId = m.id " +
            "where m.id IN ?1")
    List<UmsRoleDTO> findRoleByMenuId(String menuId);
}