package com.eteng.scaffolding.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @FileName MockMvcTest
 * @Author eTeng
 * @Date 2019/12/30 16:10
 * @Description
 */
public class MockMvcTest extends Junit5Test {

    @Autowired
    protected ObjectMapper objectMapper;
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

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        MockMvcTestExecutor.setMockMvc(mockMvc);
    }
}
