package com.passion.practice.springbootsecurity.service.impl;

import com.passion.practice.springbootsecurity.dao.SysUserRepository;
import com.passion.practice.springbootsecurity.entity.SysUser;
import com.passion.practice.springbootsecurity.security.service.CustomUserDetailsServiceImpl;
import com.passion.practice.springbootsecurity.security.utils.JwtTokenUtil;
import com.passion.practice.springbootsecurity.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @description: 用户
 * @author: lsl
 * @date: 2020-07-09
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private CustomUserDetailsServiceImpl customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Override
    public SysUser findByUserName(String username) {
        return sysUserRepository.findOneByLogin(username);
    }

    @Override
    public void update(SysUser sysUser) {
        sysUserRepository.save(sysUser);
    }

    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);

        } catch (AuthenticationException e) {
            LOGGER.error("登录异常:{}", e.getMessage());
        }
        return token;
    }
}
