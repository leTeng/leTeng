package com.eteng.scaffolding.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.test.MockMvcTestExecutor;
import com.eteng.scaffolding.test.MockMvcTestHeader;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

class UmsResourceControllerTest extends MockMvcTestHeader<UmsResource> {

    public UmsResourceControllerTest() {
        super("/umsResource", "402880396f55c0b7016f55c0cda60000");
    }

    @Test
    public void download() throws Exception{
        super.download();
    }

    @Test
    public void testDownload() throws Exception{
        super.testDownload();
    }

    @Test
    public void list() throws Exception{
        super.list(new MockMvcTestExecutor.TestOptions() {
            @Override
            public void params(MockHttpServletRequestBuilder builders) {
                builders.param("size","10")
                        .param("number","0");
            }
            @Override
            public void result(ResultActions resultActions) throws Exception {
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id").isNotEmpty());
            }
        });
    }

    @Test
    public void one() throws Exception{
        super.one();
    }

    @Test
    public void create() throws Exception{
        super.create();
    }

    @Test
    public void update() throws Exception{
        super.update();
    }

    @Test
    public void delete() throws Exception{
        super.delete();
    }

    @Override
    protected ContentFactory<UmsResource> contentFactoryBean() {
        return new ContentFactory<UmsResource>() {
            @Override
            public UmsResource createContent() {
                UmsResource resource = new UmsResource();
                resource.setType(0);
                resource.setUri("/umsResource");
                resource.setMethod("GET");
                resource.setDescription("添加资源接口");
                return resource;
            }

            @Override
            public void updateContent(UmsResource resource) {
                resource.setType(0);
                resource.setUri("/umsResource");
                resource.setMethod("DELETE");
                resource.setDescription("删除资源接口");
            }

            @Override
            public List<UmsResource> exportContent() {
                ArrayList<UmsResource> resources = CollUtil.newArrayList();
                UmsResource resource = new UmsResource();
                resource.setType(0);
                resource.setUri("/umsResource");
                resource.setMethod("POST");
                resource.setDescription("添加资源接口");
                resources.add(resource);

                UmsResource resource1 = new UmsResource();
                resource1.setType(0);
                resource1.setUri("/umsResource");
                resource1.setMethod("PUT");
                resource1.setDescription("修改资源接口");
                resources.add(resource1);
                return resources;
            }
        };
    }
}