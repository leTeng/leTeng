package com.eteng.scaffolding.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import com.eteng.scaffolding.test.MockMvcTestExecutor;
import com.eteng.scaffolding.test.MockMvcTestHeader;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

class UmsRoleControllerTest extends MockMvcTestHeader {

    public UmsRoleControllerTest() {
        super("/umsRole", "402880396f55cabe016f55cad4690000");
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
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()").value(1));
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

    /**
     * 绑定用户测试
     * @throws Exception
     */
    @Test
    public void bindingUser() throws Exception{
        MockMvcTestExecutor.post("/user/binding", new MockMvcTestExecutor.TestOptions() {
            @Override
            public String content() throws Exception {
                OneToManyForm form = new OneToManyForm();
                form.setOne("402880386f456375016f4564bc4d0001");
                ArrayList<String> userIds = CollUtil.newArrayList();
//                userIds.add("402880396f549da1016f549db76c0000");
//                userIds.add("402880396f5b50a7016f5b50b91d0000");
                userIds.add("402880396f54b62a016f54b63e3f0000");
                form.setMany(userIds);
                String content = objectMapper.writeValueAsString(form);
                return content;
            }
        });
    }

    @Override
    protected ContentFactory<UmsRole> contentFactoryBean() {
        return new ContentFactory<UmsRole>() {
            @Override
            public UmsRole createContent() {
                UmsRole umsRole = new UmsRole();
                umsRole.setName("12345");
                umsRole.setDescription("测试添加角色");
                return umsRole;
            }

            @Override
            public void updateContent(UmsRole umsRole) {
                umsRole.setName("123456");
                umsRole.setDescription("测试添加角色2");
            }

            @Override
            public List<UmsRole> exportContent() {
                List<UmsRole> roles = CollUtil.newArrayList();
                UmsRole umsRole1 = new UmsRole();
                umsRole1.setName("12345");
                umsRole1.setDescription("测试添加角色");
                roles.add(umsRole1);
                UmsRole umsRole2 = new UmsRole();
                umsRole2.setName("123456");
                umsRole2.setDescription("测试添加角色2");
                roles.add(umsRole2);
                return roles;
            }
        };
    }

    @Test
    void bindingMenu() throws Exception{
        MockMvcTestExecutor.post("/menu/binding", new MockMvcTestExecutor.TestOptions() {
            @Override
            public String content() throws Exception {
                OneToManyForm form = new OneToManyForm();
                form.setOne("402880386f456375016f4564bc4d0001");
                ArrayList<String> userIds = CollUtil.newArrayList();
                userIds.add("402880396f55d7ab016f55d7c3110000");
                form.setMany(userIds);
                String content = objectMapper.writeValueAsString(form);
                return content;
            }
        });
    }

    @Test
    void bindingResource() throws Exception{
        MockMvcTestExecutor.post("/resource/binding", new MockMvcTestExecutor.TestOptions() {
            @Override
            public String content() throws Exception {
                OneToManyForm form = new OneToManyForm();
                form.setOne("402880386f456375016f4564bc4d0001");
                ArrayList<String> userIds = CollUtil.newArrayList();
                userIds.add("402880396f55c0b7016f55c0cda60000");
                form.setMany(userIds);
                String content = objectMapper.writeValueAsString(form);
                return content;
            }
        });
    }
}