package com.passion.practice.springbootsecurity.service;

import com.passion.practice.springbootsecurity.entity.SysPermission;

import java.util.List;

/**
 *
 * @author: lsl
 * @date: 2020-07-09
 */
public interface SysPermissionService {

    List<SysPermission> findAll();
}
