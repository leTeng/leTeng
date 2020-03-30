package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsRoleResource;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
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
public interface UmsRoleResourceRepository extends JpaRepository<UmsRoleResource, String>, JpaSpecificationExecutor<UmsRoleResource> {

    UmsRoleResource findByRoleIdAndResourceIdAndDel (UmsRole roleId, UmsResource resourceId, Integer del);

    /**
     * 通过多个角色id查询资源
     * @param roleIds
     * @return
     */
    @Query("select new com.eteng.scaffolding.admin.service.dto.UmsResourceDTO(rs.id,rs.createTime,rs.updateTime,rs.del,rs.uri,rs.method,rs.type,rs.description)" +
            "from UmsRoleResource rr " +
            "left join rr.roleId r on r.id = rr.roleId " +
            "left join rr.resourceId rs on rs.id = rr.resourceId " +
            "where r.id in ?1")
    List<UmsResourceDTO> findResourceByRoleIds(Collection<String> roleIds);

    /**
     * 通过资源id查询角色
     * @param resourceId 资源id
     * @return
     */
    @Query("select new com.eteng.scaffolding.admin.service.dto.UmsRoleDTO(r.id,r.createTime,r.updateTime,r.del,r.name,r.tag,r.permission,r.description)" +
            "from UmsRoleResource rr " +
            "left join rr.roleId r on r.id = rr.roleId " +
            "left join rr.resourceId rs on rs.id = rr.resourceId " +
            "where rs.id in ?1")
    List<UmsRoleDTO> findRoleByResourceId(String resourceId);

    /**
     * 通过多个角色id查询的角色资源的关系数据
     * @param roles
     * @return
     */
    List<UmsRoleResource> findByRoleIdIn(Collection<UmsRole> roles);
}