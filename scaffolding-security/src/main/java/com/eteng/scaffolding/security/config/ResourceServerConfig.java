package com.eteng.scaffolding.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 资源服务配置
 * @FileName ResourceServerConfig
 * @Author eTeng
 * @Date 2020/1/8 12:39
 * @Description
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler myAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/actuator/**","/doc.html","/webjars/**","/swagger-resources/**","/v2/**").permitAll()
            .anyRequest().authenticated()
                .and()
            .formLogin().permitAll()
                .and()
            .exceptionHandling();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore).resourceId("WEBS");
        // 配置身份验证异常处理器
        resources.authenticationEntryPoint(myAuthenticationEntryPoint)
                // 配置鉴权异常处理器
                .accessDeniedHandler(myAccessDeniedHandler);
    }
}
