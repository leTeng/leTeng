package com.eteng.scaffolding.generator.rest;

import com.eteng.scaffolding.common.dto.CommonPage;
import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.generator.domain.vo.ColumnInfo;
import com.eteng.scaffolding.generator.domain.vo.TableInfo;
import com.eteng.scaffolding.test.MockMvcTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@EnableTransactionManagement
class GeneratorControllerTest extends MockMvcTest {

    public static MockHttpServletRequestBuilder API_GENERATOR_TABLES_GET = MockMvcRequestBuilders
            .get("/api/generator/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .param("page","0")
            .param("size","10");

    public static MockHttpServletRequestBuilder API_GENERATOR_COLUMNS_GET = MockMvcRequestBuilders
            .get("/api/generator/columns")
            .contentType(MediaType.APPLICATION_JSON)
            .param("tableName","tb_base");

    public static MockHttpServletRequestBuilder API_GENERATOR_POST = MockMvcRequestBuilders
            .post("/api/generator")
            .contentType(MediaType.APPLICATION_JSON)
            .param("tableName","tb_base");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    /**
     * 使用Junit5使用@BeforeEach来代替@Before
     */
    @Before
    public void setUp(){
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(wac);
        mockMvc = builder.build();
    }

    /**
     * 查询数据库表接口测试
     * @throws Exception
     */
    @Test
    void getTables() throws Exception{
        mockMvc.perform(API_GENERATOR_TABLES_GET)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value("1"))
                .andDo(print());
    }

    /**
     * 查询指定表的列信息接口测试
     * @throws Exception
     */
    @Test
    void testGetTables() throws Exception{
        mockMvc.perform(API_GENERATOR_COLUMNS_GET)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$..content[0].columnName").value("id"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..content[1].columnName").value("create_date"));
    }

    /**
     * 自动生成代码接口测试
     * @throws Exception
     */
    @Test
    void generator() throws Exception{
        // 获取表信息
        mockMvc.perform(API_GENERATOR_TABLES_GET)
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("?(@.content.length() > 0)").value(Boolean.TRUE))
                .andDo(mvcResult -> {
                    String result = mvcResult.getResponse().getContentAsString();
                    CommonResult<CommonPage<TableInfo>> tablePage = objectMapper.readValue(result, new TypeReference<CommonResult<CommonPage<TableInfo>>>(){});
                    List<TableInfo> tables = tablePage.getData().getList();
                    // 获取指定表的列数据
                    final String tableName = (String) tables.get(0).getTableName();
                    mockMvc.perform(API_GENERATOR_COLUMNS_GET)
                            .andExpect(MockMvcResultMatchers.status().isOk())
//                            .andExpect(MockMvcResultMatchers.jsonPath("?(@.totalElements > 0)").value(Boolean.TRUE))
                            .andDo(r -> {
                                String columnsResult = r.getResponse().getContentAsString();
                                CommonResult<CommonPage<ColumnInfo>> columnsPage = objectMapper.readValue(columnsResult, new TypeReference<CommonResult<CommonPage<ColumnInfo>>>(){});
                                List<ColumnInfo> columns = columnsPage.getData().getList();
                                // 修改生成配置
                                mockMvc.perform(GenConfigControllerTest.API_GEN_CONFIGGET_POST)
                                        .andExpect(MockMvcResultMatchers.status().isOk());
                                // 生成配置
                                mockMvc.perform(API_GENERATOR_POST.content(objectMapper.writeValueAsString(columns)))
                                        .andExpect(MockMvcResultMatchers.status().isOk());
                            });
                });
    }
}