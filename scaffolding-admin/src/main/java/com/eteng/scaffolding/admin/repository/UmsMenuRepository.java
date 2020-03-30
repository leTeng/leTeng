package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.pojo.UmsMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsMenuRepository extends JpaRepository<UmsMenu, String>, JpaSpecificationExecutor<UmsMenu> {

    UmsMenu findByUriAndDel (String uri,Integer del);
}