package com.eteng.scaffolding.test;

import cn.hutool.core.util.ObjectUtil;
import org.junit.Assert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * 测试执行器
 * @FileName TestExecutor
 * @Author eTeng
 * @Date 2019/12/31 12:00
 * @Description
 */
public class MockMvcTestExecutor {

    private static String context;
    private static MockMvc mockMvc;
    public static String getContext() {
        return context;
    }

    public static void setContext(String context) {
        MockMvcTestExecutor.context = context;
    }

    public static MockMvc getMockMvc() {
        return mockMvc;
    }

    public static void setMockMvc(MockMvc mockMvc) {
        MockMvcTestExecutor.mockMvc = mockMvc;
    }

    public static void post(String uri) throws Exception{
        post(uri,null);
    }

    public static void post(String uri,TestOptions testOptions)throws Exception{
        executor(filter(MockMvcRequestBuilders.post(context + uri)),testOptions);

    }

    public static void get(String uri)throws Exception{
        get(uri,null);
    }

    public static void get(String uri,TestOptions testOptions)throws Exception{
        executor(filter(MockMvcRequestBuilders.get(context + uri)),testOptions);
    }

    public static void put(String uri)throws Exception{
        put(uri,null);
    }

    public static void put(String uri,TestOptions testOptions)throws Exception{
        executor(filter(MockMvcRequestBuilders.put(context + uri)),testOptions);
    }

    public static void delete(String uri)throws Exception{
        delete(uri,null);
    }

    public static void delete(String uri,TestOptions testOptions)throws Exception{
        executor(filter(MockMvcRequestBuilders.delete(context + uri)),testOptions);
    }

    public static void executor(MockHttpServletRequestBuilder filter,TestOptions testOptions)throws Exception{
        if(ObjectUtil.isNotNull(testOptions)){
            testOptions.params(filter);
            String content = testOptions.content();
            if(content != null){
                filter.content(content);
            }
        }
        Assert.assertNotNull(mockMvc);
        ResultActions perform = mockMvc.perform(filter);
        filter(perform);
        if(ObjectUtil.isNotNull(testOptions)){
            testOptions.result(perform);
        }
    }

    /**
     * 补充结果验证
     * @param resultActions
     * @return
     */
    private static ResultActions filter(ResultActions resultActions)throws Exception{
        return resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200));
    }

    /**
     * 补充请求头
     * @param builder
     * @return
     */
    private static MockHttpServletRequestBuilder filter(MockHttpServletRequestBuilder builder){
        return builder.contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8");
    }

    /**
     * 测试关注点
     */
    public static abstract class TestOptions<T>{
        /**
         * 查询参数补充
         * @param builders
         */
        public void  params(MockHttpServletRequestBuilder builders){

        }

        /**
         * 请求实体补充
         */
        public String content()throws Exception{
            return null;
        }

        /**
         * 结果处理
         * @param resultActions
         */
        public void result(ResultActions resultActions) throws Exception{

        }
    }
}
