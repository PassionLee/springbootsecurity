package com.passion.practice.springbootsecurity.service.impl;

import com.passion.practice.springbootsecurity.dao.SysPermissionRepository;
import com.passion.practice.springbootsecurity.entity.SysPermission;
import com.passion.practice.springbootsecurity.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: lsl
 * @date: 2020-07-09
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    SysPermissionRepository sysPermissionRepository;

    @Override
    public List<SysPermission> findAll() {
        return sysPermissionRepository.findAll();
    }
}
