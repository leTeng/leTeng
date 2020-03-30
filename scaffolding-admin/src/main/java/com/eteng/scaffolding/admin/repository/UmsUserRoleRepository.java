package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsUser;
import com.eteng.scaffolding.admin.pojo.UmsUserRole;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsUserRoleRepository extends JpaRepository<UmsUserRole, String>, JpaSpecificationExecutor<UmsUserRole> {

    UmsUserRole findByUserIdAndRoleIdAndDel (UmsUser userId, UmsRole roleId, Integer del);

    /**
     * 通过用户查询所拥有的角色
     * @param userId 用户id
     * @return 角色DTO
     */
    @Query("select new com.eteng.scaffolding.admin.service.dto.UmsRoleDTO(r.id,r.createTime,r.updateTime,r.del,r.name,r.tag,r.permission,r.description) " +
            "from UmsUserRole ur " +
            "left join ur.userId u on u.id = ur.userId " +
            "left join ur.roleId r on r.id = ur.roleId " +
            "where u.id = ?1")
    List<UmsRoleDTO> finRoledByUserId(String userId);

    /**
     * 通过角色查询所拥有的用户
     * @param roleId
     * @return
     */
    @Query("select new com.eteng.scaffolding.admin.service.dto.UmsUserDTO(u.id,u.createTime,u.updateTime,u.del,u.userName,u.phone,u.lastLogin,u.type,u.typeId) " +
            "from UmsUserRole ur " +
            "left join ur.userId u on u.id = ur.userId " +
            "left join ur.roleId r on r.id = ur.roleId " +
            "where r.id = ?1")
    List<UmsUserDTO> findUserByRoleId(String roleId);
}