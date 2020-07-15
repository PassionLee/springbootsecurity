package com.passion.practice.springbootsecurity.security.service;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * 自定义权限
 * @author: lsl
 * @date: 2020-07-09
 */
public interface CustomPermissionService {

    /**
     * 从数据源加载需要限制访问的资源
     * @return
     */
    Map<String, ConfigAttribute> loadFromDataSource();
}
