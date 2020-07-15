package com.passion.practice.springbootsecurity.config;

import com.passion.practice.springbootsecurity.security.IgnoreUrlsConfig;
import com.passion.practice.springbootsecurity.security.filter.CustomAbstractSecurityInterceptor;
import com.passion.practice.springbootsecurity.security.filter.JwtAuthenticationTokenFilter;
import com.passion.practice.springbootsecurity.security.handler.*;
import com.passion.practice.springbootsecurity.security.service.CustomUserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @description: security config
 * @author: lsl
 * @date: 2020-07-08
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER   = LoggerFactory.getLogger(SecurityConfiguration.class);

    /**
     * 重写父类userDetailService 为自定义的实现
     * 用于获取用户登录信息
     *
     * @return userDetailsService
     */
    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    /**
     * 未登录或者登录过期返回
     */
    @Autowired
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * 加载权限数据
     */
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    /**
     * jwt 过滤器
     */
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     * 权限决策处理器
     */
    @Autowired
    CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * 基于url的权限校验拦截器
     */
    @Autowired
    CustomAbstractSecurityInterceptor customAbstractSecurityInterceptor;

    /**
     * 密码加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 所有忽略的url配置
     *
     * @return IgnoreUrlsConfig
     */
    @Bean
    IgnoreUrlsConfig ignoreUrlsConfig() {
        return new IgnoreUrlsConfig();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /*
        http.authorizeRequests()
                .antMatchers("/product/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin();
        // 设置security 为无状态
        // always – a session will always be created if one doesn’t already exist，没有session就创建。
        // ifRequired – a session will be created only if required (default)，如果需要就创建（默认）。
        // never – the framework will never create a session itself but it will use one if it already exists
        // stateless – no session will be created or used by Spring Security 不创建不使用session
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        */

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();

        // 不需要保护的资源路径允许访问
        for (String url : ignoreUrlsConfig().getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        // 允许跨域请求OPTIONS请求
        registry.antMatchers(HttpMethod.OPTIONS).permitAll();

        // 任何请求需要身份认证
        registry.and().authorizeRequests().anyRequest().authenticated()
                // 关不跨站请求防护
                .and().csrf().disable()
                // 不使用session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 自定义权限拒绝处理器
                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                // 自定义权限拒绝返回
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                // 自定义权限拦截过滤器
                .and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 有权限配置时，添加基于url的权限校验
        registry.and().addFilterBefore(customAbstractSecurityInterceptor, FilterSecurityInterceptor.class);


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 从内存加载用户
        /*
        auth.inMemoryAuthentication()
                .withUser("admin1").password("{noop}admin1").roles("ADMIN", "USER")
                .and()
                .withUser("user1").password("{noop}user1").roles("USER");
        */

        // 设置自定义userDetailsService
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

}
