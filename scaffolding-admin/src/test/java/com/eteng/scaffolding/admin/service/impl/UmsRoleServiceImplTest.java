package com.eteng.scaffolding.admin.service.impl;

import com.eteng.scaffolding.admin.service.UmsRoleService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleQueryCriteria;
import com.eteng.scaffolding.test.Junit5Test;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

class UmsRoleServiceImplTest extends Junit5Test {


    @Autowired
    private UmsRoleService umsRoleService;

    @Test
    void queryAll() {
    }

    @Test
    void testQueryAll() {
        List<UmsRoleDTO> umsRoleDTOS = umsRoleService.queryAll(UmsRoleQueryCriteria.builder().build());
        Assert.assertNotNull(umsRoleDTOS);
    }

    @Test
    void findById() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void download() {
    }
}