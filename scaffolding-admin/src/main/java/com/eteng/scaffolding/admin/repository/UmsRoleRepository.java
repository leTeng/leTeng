package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsRoleRepository extends JpaRepository<UmsRole, String>, JpaSpecificationExecutor<UmsRole> {

    /**
     * @param name
     * @param tag
     * @param del
     * @return
     */
    UmsRole findByNameAndTagAndDel (String name,String tag,Integer del);
}