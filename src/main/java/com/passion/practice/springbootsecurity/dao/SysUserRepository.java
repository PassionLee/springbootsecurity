package com.passion.practice.springbootsecurity.dao;

import com.passion.practice.springbootsecurity.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @description: user dao
 * @author: lsl
 * @date: 2020-07-08
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {

    SysUser findOneByLogin(String login);
}
