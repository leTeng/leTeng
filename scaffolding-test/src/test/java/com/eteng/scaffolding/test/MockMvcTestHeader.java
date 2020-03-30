package com.eteng.scaffolding.test;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * 接口层测试的基层类，封装了基本接口的测试方法
 * @FileName com.eteng.scaffolding.test.MockMvcTestHeader
 * @Author eTeng
 * @Date 2019/12/17 14:24
 * @Description
 */
public abstract class MockMvcTestHeader<T> extends MockMvcTest {

    private static String id;

    public MockMvcTestHeader(String context,String id) {
        Assert.assertNotNull(context);
        MockMvcTestExecutor.setContext(context);
        Assert.assertNotNull(id);
        MockMvcTestHeader.id = id;
    }

    /**
     * 测试根据条件导出接口
     * @throws Exception
     */
    protected void download() throws Exception{
        MockMvcTestExecutor.get("/download", new MockMvcTestExecutor.TestOptions() {
            @Override
            public void params(MockHttpServletRequestBuilder builders) {
                builders.param("size","10").param("number","0");
            }
        });
    }

    /**
     * 测试根据选定内容导出接口
     * @throws Exception
     */
    protected void testDownload() throws Exception{
        MockMvcTestExecutor.post("/download", new MockMvcTestExecutor.TestOptions() {
            @Override
            public String content() throws Exception{
                ContentFactory<T> factory = contentFactoryBean();
                List<T> buidlers = factory.exportContent();
                return objectMapper.writeValueAsString(buidlers);
            }
        });
    }

    /**
     * 测试根据条件查询分页列表接口
     * @throws Exception
     */
    protected void list(MockMvcTestExecutor.TestOptions testOptions) throws Exception{
        MockMvcTestExecutor.get("",testOptions);
    }

    /**
     * 测试根据id查询详情接口
     * @throws Exception
     */
    protected void one() throws Exception{
        MockMvcTestExecutor.get("/" + id, new MockMvcTestExecutor.TestOptions() {
            @Override
            public void result(ResultActions resultActions) throws Exception {
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty());
            }
        });
    }

    /**
     * 测试添加数据接口
     * @throws Exception
     */
    protected void create() throws Exception{
        MockMvcTestExecutor.post("", new MockMvcTestExecutor.TestOptions() {
            @Override
            public String content() throws Exception {
                ContentFactory<T> factory = contentFactoryBean();
                Assert.assertNotNull(factory);
                T t = factory.createContent();
                Assert.assertNotNull(t);
                return objectMapper.writeValueAsString(t);
            }
            @Override
            public void result(ResultActions resultActions) throws Exception {
                resultActions.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty());
            }
        });
    }

    /**
     * 测试修改数据接口
     * @throws Exception
     */
    protected void update() throws Exception{
        MockMvcTestExecutor.get("/" + id, new MockMvcTestExecutor.TestOptions() {
            @Override
            public void result(ResultActions resultActions) throws Exception {
                MvcResult mvcResult = resultActions.andReturn();
                String result = mvcResult.getResponse().getContentAsString();
                CommonResult<T> commonResult = getObjectMapper().readValue(result, new TypeReference<CommonResult<T>>(){});
                Object object = commonResult.getData();
                String body = getObjectMapper().writeValueAsString(object);
                T t = getObjectMapper().readValue(body, getTClass());
                // 手动修改
                ContentFactory<T> factory = contentFactoryBean();
                factory.updateContent(t);
                String content = objectMapper.writeValueAsString(t);
                MockMvcTestExecutor.put("", new MockMvcTestExecutor.TestOptions() {
                    @Override
                    public String content() throws Exception {
                        return content;
                    }
                });
            }
        });
    }

    /**
     * 测试删除数据接口
     * @throws Exception
     */
    protected void delete() throws Exception{
        MockMvcTestExecutor.delete("/" + id);
    }

    /**
     * 获取测试实体工厂Bean
     * @return
     */
    protected abstract ContentFactory<T> contentFactoryBean();

    /**
     * 表单实体工厂
     * @param <T>
     */
    public interface ContentFactory<T>{

        /**
         * 获取新添加的表单数据
         * @return
         */
        T createContent();

        /**
         * 修改表单数据
         * @param t
         */
        void updateContent(T t);

        /**
         * 导出的表单数据
         * @return
         */
        List<T> exportContent();
    }

    private Class<T> getTClass() {
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
