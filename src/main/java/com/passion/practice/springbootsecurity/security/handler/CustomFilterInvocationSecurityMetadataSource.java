package com.passion.practice.springbootsecurity.security.handler;

import com.passion.practice.springbootsecurity.security.service.CustomPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 自定义权限数据源，用于获取当前访问需要的资源列表
 *
 * @author: lsl
 * @date: 2020-07-09
 */
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomFilterInvocationSecurityMetadataSource.class);

    /**
     * 负责从数据源中加载访问需要资源列表
     */
    @Autowired
    private CustomPermissionService customPermissionService;

    /**
     * 定义资源集合
     */
    private static Map<String, ConfigAttribute> configAttributeMap = null;


    /**
     * PostConstruct 该注解被用来修饰一个非静态的void（）方法。
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
     * PostConstruct在构造函数之后执行，init方法之前执行。
     * 通常我们会是在Spring框架中使用到@PostConstruct注解 该注解的方法在整个Bean初始化中的执行顺序：
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     * <p>
     * 从数据源加载资源列表
     */
    @PostConstruct
    public void loadFormDataSource() {
        configAttributeMap = customPermissionService.loadFromDataSource();
    }

    /**
     * 清除已加载，在修改系统配置权限时调用
     */
    public void clearDataSource() {
        configAttributeMap.clear();
        configAttributeMap = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        if (configAttributeMap == null) {
            this.loadFormDataSource();
        }

        List<ConfigAttribute> configAttributes = new ArrayList<>();
        // 获取当前访问的路径
        String url = ((FilterInvocation) o).getRequestUrl();
        LOGGER.info("当前请求 url: {}", url);

        // 遍历系统设置资源列表，如果当前url与之匹配，这加入操作需要权限列表中
        PathMatcher pathMatcher = new AntPathMatcher();
        Iterator<String> iterator = configAttributeMap.keySet().iterator();
        while (iterator.hasNext()) {
            String pattern = iterator.next();
            // example: pattern: /admin/** , url: /admin/delete add value of map key /admin/** to collection
            if (pathMatcher.match(pattern, url)) {
                configAttributes.add(configAttributeMap.get(pattern));
            }
        }
        // 未设置操作请求权限，返回空集合
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
