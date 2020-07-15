package com.passion.practice.springbootsecurity.service;

import com.passion.practice.springbootsecurity.entity.SysUser;

/**
 *
 * @author: lsl
 * @date: 2020-07-09
 */
public interface SysUserService {

    SysUser findByUserName(String username);

    void update(SysUser sysUser);

    String login(String username, String password);
}
