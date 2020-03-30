package com.eteng.scaffolding.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsUser;
import com.eteng.scaffolding.admin.pojo.UmsUserRole;
import com.eteng.scaffolding.test.MockMvcTestExecutor;
import com.eteng.scaffolding.test.MockMvcTestHeader;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

class UmsUserControllerTest extends MockMvcTestHeader<UmsUser> {

    public UmsUserControllerTest() {
        super("/umsUser", "402880396f54b62a016f54b63e3f0000");
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
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.list.length()").value(2));
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
    protected ContentFactory<UmsUser> contentFactoryBean() {
        return new ContentFactory<UmsUser>() {
            @Override
            public UmsUser createContent() {
                UmsUser umsUser = new UmsUser();
                umsUser.setPhone("1234567");
                umsUser.setUserName("1234567");
                umsUser.setType(0);
                umsUser.setTypeId("1234567");
                umsUser.setCredentials("1234567");
                List<UmsUserRole> umsUserRoles = new ArrayList<>();

                UmsUserRole userRole1 = new UmsUserRole();
                userRole1.setUserId(umsUser);
                UmsRole role = new UmsRole();
                role.setId("402880396f54b1d6016f54b1ea1a0000");
                userRole1.setRoleId(role);
                umsUserRoles.add(userRole1);

                UmsUserRole userRole2 = new UmsUserRole();
                userRole2.setUserId(umsUser);
                UmsRole role2 = new UmsRole();
                role.setId("402880396f54b1d6016f54b1ea1a0000");
                userRole2.setRoleId(role2);
                umsUserRoles.add(userRole2);

                return umsUser;
            }

            @Override
            public void updateContent(UmsUser umsUser) {
                umsUser.setPhone("12345_u");
                umsUser.setUserName("12345_u");
                umsUser.setType(0);
                umsUser.setTypeId("12345_u");
                umsUser.setCredentials("12345_u");
            }

            @Override
            public List<UmsUser> exportContent() {
                List<UmsUser> umsUsers = CollUtil.newArrayList();
                UmsUser umsUser = new UmsUser();
                umsUser.setPhone("4321");
                umsUsers.add(umsUser);
                UmsUser umsUser1 = new UmsUser();
                umsUser.setPhone("1234");
                umsUsers.add(umsUser1);
                return umsUsers;
            }
        };
    }
}