package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.pojo.UmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author eTeng
* @date 2019-12-27
*/
public interface UmsUserRepository extends JpaRepository<UmsUser, String>, JpaSpecificationExecutor<UmsUser> {

    UmsUser findByUserNameAndDel (String userName,Integer del);

    UmsUser findByTypeIdAndDel (String typeId,Integer del);

    UmsUser findByPhoneAndDel (String phone,Integer del);

    UmsUser findByUserName(String userName);

    UmsUser findByPhone(String phone);
}