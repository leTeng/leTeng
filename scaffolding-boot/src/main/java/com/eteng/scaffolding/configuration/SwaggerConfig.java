package com.eteng.scaffolding.configuration;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.knife4j.spring.annotations.EnableSwaggerBootstrapUi;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Swagger 配置
 * @FileName SwaggerConfig
 * @Author eTeng
 * @Date 2020/1/7 10:33
 * @Description
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUi
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    private final TypeResolver typeResolver;

    @Autowired
    public SwaggerConfig(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }


    /**
     * Swagger分页参数转换
     * @param resolver
     * @return
     */
    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return CollUtil.newArrayList(new AlternateTypeRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)));
            }
        };
    }

    /**
     * 权限模块接口文档配置
     * @return
     */
    @Bean(value = "adminApi")
    public Docket adminApi() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("权限管理接口文档","权限管理接口文档","http://localhost:8080","eTeng","1.0.0"))
                .groupName("权限管理接口文档")
                .select()
                // 根据包路径指定接口位置
                .apis(RequestHandlerSelectors.basePackage("com.eteng.scaffolding.admin.controller"))
                //根据注解来指定接口位置
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                // 配置全局请求参数
                .build()
//                .globalOperationParameters(globParameter())
                // 是否启用swagger,可以根据环境来动态改动
//                .enable(enable)
                // 配置授权请求头
                .securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
        return docket;
    }

    /**
     * 授权模块接口文档配置
     * @return
     */
    @Bean(value = "securityApi")
    public Docket securityApi() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("授权接口文档","授权接口文档","http://localhost:8080/","eTeng","1.0.0"))
                .groupName("授权接口文档")
                .select()
                // 根据包路径指定接口位置
                .apis(RequestHandlerSelectors.basePackage("com.eteng.scaffolding.security.controller"))
                //根据注解来指定接口位置
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                // 配置全局请求参数
                .build()
//                .globalOperationParameters(globParameter())
                // 是否启用swagger,可以根据环境来动态改动
//                .enable(enable)
                // 配置授权请求头
                .securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
        return docket;
    }

    /**
     * 小程序接口文档配置
     * @return
     */
    @Bean(value = "miniAppApi")
    public Docket miniAppApi() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo("小程序接口文档","小程序接口文档","http://localhost:8080","eTeng","1.0.0"))
                .groupName("小程序接口文档")
                .select()
                // 根据包路径指定接口位置
                .apis(RequestHandlerSelectors.basePackage("com.eteng.scaffolding.security.controller"))
                //根据注解来指定接口位置
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                // 配置全局请求参数
                .build()
//                .globalOperationParameters(globParameter())
                // 是否启用swagger,可以根据环境来动态改动
//                .enable(enable)
                // 配置授权请求头
                .securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
        return docket;
    }

    /**
     * 构建文档信息
     * @param title 文档主题
     * @param description 文档描述
     * @param termsOfServiceUrl 服务器地址
     * @param contact 联系人
     * @param vsersion 文档版本
     * @return
     */
    private ApiInfo apiInfo(String title,String description,String termsOfServiceUrl,String contact,String vsersion) {
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .termsOfServiceUrl(termsOfServiceUrl)
                .contact(contact)
                .version(vsersion)
                .build();
    }

    /**
     * 配置需要授权认证的url匹配规则
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("BearerToken", "Authorization", "header");
    }

    /**
     * 请求的全局参数配置
     * @return
     */
//    private List<Parameter> globParameter(){
//        ParameterBuilder parameterBuilder=new ParameterBuilder();
//        List<Parameter> parameters= Lists.newArrayList();
//        parameterBuilder.name("token").description("token令牌").modelRef(new ModelRef("String"))
//                .parameterType("query")
//                .required(true).build();
//        parameters.add(parameterBuilder.build());
//        return parameters;
//    }

    @ApiModel
    @Data
    static class Page {
        @ApiModelProperty("页码(1-N,默认1)")
        private Integer page;

        @ApiModelProperty("页面大小,默认10")
        private Integer size;

        @ApiModelProperty("根据字段排序(格式为: property(,asc|desc),默认根据创建时间排序)")
        private List<String> sort;
    }

}
