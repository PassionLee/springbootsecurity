package com.passion.practice.springbootsecurity.dao;

import com.passion.practice.springbootsecurity.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: lsl
 * @date: 2020-07-09
 */
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {
}
