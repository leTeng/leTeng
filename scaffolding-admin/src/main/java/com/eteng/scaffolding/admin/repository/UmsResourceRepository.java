package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.pojo.UmsResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsResourceRepository extends JpaRepository<UmsResource, String>, JpaSpecificationExecutor<UmsResource> {

    UmsResource findByUriAndMethodAndDel (String uri,String method,Integer del);
}