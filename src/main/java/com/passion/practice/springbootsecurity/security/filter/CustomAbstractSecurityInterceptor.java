package com.passion.practice.springbootsecurity.security.filter;

import com.passion.practice.springbootsecurity.security.IgnoreUrlsConfig;
import com.passion.practice.springbootsecurity.security.handler.CustomAccessDecisionManager;
import com.passion.practice.springbootsecurity.security.handler.CustomFilterInvocationSecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 自定义权限拦截器，需实现 Filter
 * 处理基于url的权限验证
 *
 * @author: lsl
 * @date: 2020-07-14
 */
@Component
public class CustomAbstractSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Autowired
    public void setMyAccessDecisionManager(CustomAccessDecisionManager accessDecisionManager) {
        super.setAccessDecisionManager(accessDecisionManager);
    }

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    /**
     * 要使用 FilterInvocation 调用过滤器链需要设置自定义 AccessDecisionManager、FilterInvocationSecurityMetadataSource 的supports()方法返回 true
     *
     * @return
     */
    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return customFilterInvocationSecurityMetadataSource;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        // OPTIONS请求直接放行
        if (request.getMethod().equals(HttpMethod.OPTIONS.toString())) {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
            return;
        }
        // 白名单请求直接放行
        PathMatcher pathMatcher = new AntPathMatcher();
        for (String path : ignoreUrlsConfig.getUrls()) {
            if (pathMatcher.match(path, request.getRequestURI())) {
                fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
                return;
            }
        }
        // 此处调用 FilterInvocationSecurityMetadataSource 的 getAttributes(Object object) 这个方法获取fi对应的所有权限
        // 然后会调用 AccessDecisionManager 中的 decide 方法进行鉴权操作
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            // 执行下一个
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

}
