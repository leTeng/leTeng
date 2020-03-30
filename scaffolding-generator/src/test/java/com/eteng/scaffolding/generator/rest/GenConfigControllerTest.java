package com.eteng.scaffolding.generator.rest;

import com.eteng.scaffolding.generator.domain.GenConfig;
import com.eteng.scaffolding.test.MockMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * 生成配置Controller测试
 */
@Slf4j
class GenConfigControllerTest extends MockMvcTest {

    private static ObjectMapper objectMapper;

    public static GenConfig config;

    public static MockHttpServletRequestBuilder API_GEN_CONFIGGET_GET = MockMvcRequestBuilders
            .get("/api/genConfig")
            .contentType(MediaType.APPLICATION_JSON);

    public static MockHttpServletRequestBuilder API_GEN_CONFIGGET_POST;

    static {
        objectMapper = new ObjectMapper();
        GenConfig config = new GenConfig();
        config.setId("1");
        config.setAuthor("eteng");
        config.setCover(false);
        config.setModuleName("scaffolding-admin");
        config.setPack("com.eteng.scaffolding.admin");
        config.setPath("e:/test");
        try{
            API_GEN_CONFIGGET_POST = MockMvcRequestBuilders
                    .put("/api/genConfig")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(config));
        }catch (Exception var1){
            log.error(var1.getMessage());
        }
    }

    @Test
    public void get() throws Exception{
        MockMvc mockMvc = this.getMockMvc();
        mockMvc.perform(API_GEN_CONFIGGET_GET)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update() throws Exception{
        MockMvc mockMvc = this.getMockMvc();
        mockMvc.perform(API_GEN_CONFIGGET_POST)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}