package com.eteng.scaffolding.admin.controller;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.pojo.UmsMenu;
import com.eteng.scaffolding.test.MockMvcTestExecutor;
import com.eteng.scaffolding.test.MockMvcTestHeader;
import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

class UmsMenuControllerTest extends MockMvcTestHeader<UmsMenu> {

    public UmsMenuControllerTest() {
        super("/umsMenu", "402880396f55d7ab016f55d7c3110000");
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
    protected ContentFactory<UmsMenu> contentFactoryBean() {
        return new ContentFactory<UmsMenu>() {
            @Override
            public UmsMenu createContent() {
                UmsMenu menu = new UmsMenu();
                menu.setName("添加菜单页面2");
                menu.setSort(0);
                menu.setEnable(true);
                menu.setUri("/umsMenu/add/index2");
                menu.setDescription("添加菜单页面2");
                return menu;
            }

            @Override
            public void updateContent(UmsMenu umsMenu) {
                umsMenu.setName("修改菜单页面1");
                umsMenu.setSort(0);
                umsMenu.setEnable(true);
                umsMenu.setUri("/umsMenu/update/index");
                umsMenu.setDescription("修改菜单页面1");
            }

            @Override
            public List<UmsMenu> exportContent() {
                List<UmsMenu> menus = CollUtil.newArrayList();
                UmsMenu menu = new UmsMenu();
                menu.setName("添加菜单页面");
                menu.setSort(0);
                menu.setEnable(true);
                menu.setUri("/umsMenu/add/index");
                menu.setDescription("添加菜单页面");
                menus.add(menu);

                UmsMenu menu1 = new UmsMenu();
                menu1.setName("修改菜单页面");
                menu1.setSort(2);
                menu1.setEnable(true);
                menu1.setUri("/umsMenu/update/index");
                menu1.setDescription("修改菜单页面");
                menus.add(menu1);
                return menus;
            }
        };
    }
}