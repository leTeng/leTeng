package com.eteng.scaffolding.security;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @FileName MockMvcTestHeader
 * @Author eTeng
 * @Date 2019/12/17 14:24
 * @Description
 */
public class MockMvcTestHeader extends Junit5TestHeader {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    public WebApplicationContext getWac() {
        return wac;
    }

    public void setWac(WebApplicationContext wac) {
        this.wac = wac;
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
}
