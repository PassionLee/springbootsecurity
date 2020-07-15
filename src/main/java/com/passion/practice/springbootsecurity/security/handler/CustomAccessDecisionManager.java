package com.passion.practice.springbootsecurity.security.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 自定义权限决策管理器，用于判断用户是否有访问权限
 * @author: lsl
 * @date: 2020-07-09
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
        LOGGER.info("调用自定义权限决策管理器 CustomAccessDecisionManager");
        // 当接口未被配置资源时直接放行
        if (collection.isEmpty()) {
            return;
        }
        LOGGER.info("当前接口配置资源 collection size: {}", collection.size());
        Iterator<ConfigAttribute> iterator = collection.iterator();
        // 遍历访问所需资源和用户拥有资源进行比对
        int n = 1;
        while (iterator.hasNext()) {
            ConfigAttribute configAttribute = iterator.next();
            String needAuthority = configAttribute.getAttribute();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                LOGGER.info("当前用户拥有权限: {}, 需要资源{}: {}", grantedAuthority, n, needAuthority);
                LOGGER.info("authentication authorities: {}, need authority: {}", grantedAuthority, needAuthority);
                if (needAuthority.trim().equals(grantedAuthority.getAuthority())) {
                    return;
                }
            }
            n++;
        }
        throw new AccessDeniedException("抱歉，您没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
