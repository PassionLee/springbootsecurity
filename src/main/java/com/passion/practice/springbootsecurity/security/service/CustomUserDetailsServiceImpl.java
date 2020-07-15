package com.passion.practice.springbootsecurity.security.service;

import com.passion.practice.springbootsecurity.dao.SysUserRepository;
import com.passion.practice.springbootsecurity.entity.SysPermission;
import com.passion.practice.springbootsecurity.entity.SysUser;
import com.passion.practice.springbootsecurity.service.SysPermissionService;
import com.passion.practice.springbootsecurity.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: security user service
 * @author: lsl
 * @date: 2020-07-08
 */
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysPermissionService sysPermissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1.查询用户
        SysUser userFormDb = sysUserService.findByUserName(username);
        if (null == userFormDb) {
            throw new UsernameNotFoundException("user " + username + " was not found ");
        }
        // 2.设置角色
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + userFormDb.getRole());

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("1:产品管理");
        grantedAuthorities.add(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(username, userFormDb.getPassword(), grantedAuthorities);
    }

    /**
     * 注入自定义资源
     *
     * @return CustomPermissionService
     */
    @Bean
    public CustomPermissionService customPermissionService() {
        return () -> {
            Map<String, ConfigAttribute> map = new ConcurrentHashMap<>();
            if (null != sysPermissionService) {
                List<SysPermission> resourceList = sysPermissionService.findAll();
                for (SysPermission resource : resourceList) {
                    map.put(resource.getUrl(), new org.springframework.security.access.SecurityConfig(resource.getId() + ":" + resource.getName()));
                }
            }
            return map;
        };
    }
}
