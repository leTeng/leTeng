package com.eteng.scaffolding.admin.service.impl;

import com.eteng.scaffolding.admin.service.UmsMenuService;
import com.eteng.scaffolding.admin.service.dto.UmsMenuQueryCriteria;
import com.eteng.scaffolding.test.Junit5Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UmsMenuServiceImplTest extends Junit5Test {

    @Autowired
    private UmsMenuService umsMenuService;

    @Test
    void queryAll() {
    }

    @Test
    void testQueryAll() {
        umsMenuService.queryAll(UmsMenuQueryCriteria.builder().build());
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