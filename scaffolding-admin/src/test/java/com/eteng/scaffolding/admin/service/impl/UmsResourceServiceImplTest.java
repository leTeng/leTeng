package com.eteng.scaffolding.admin.service.impl;

import com.eteng.scaffolding.admin.service.UmsResourceService;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsResourceQueryCriteria;
import com.eteng.scaffolding.test.Junit5Test;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


class UmsResourceServiceImplTest extends Junit5Test {

    @Autowired
    private UmsResourceService umsResourceService;

    @Test
    void queryAll() {
    }

    @Test
    void testQueryAll() {
        List<UmsResourceDTO> umsResourceDTOS = umsResourceService.queryAll(UmsResourceQueryCriteria.builder().build());
        Assert.assertNotNull(umsResourceDTOS);
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