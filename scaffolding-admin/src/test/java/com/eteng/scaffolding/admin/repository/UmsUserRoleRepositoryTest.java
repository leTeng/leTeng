package com.eteng.scaffolding.admin.repository;

import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.test.Junit5Test;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class UmsUserRoleRepositoryTest extends Junit5Test {

    @Autowired
    private UmsUserRoleRepository umsUserRoleRepository;

    @Test
    public void testFindRoleByUserId(){
        List<UmsRoleDTO> umsRoleDTOS = umsUserRoleRepository.finRoledByUserId("402880396f549da1016f549db76c0000");
        Assert.assertNotNull(umsRoleDTOS);
        log.debug(umsRoleDTOS.toString());
    }
}