package com.passion.practice.springbootsecurity.security.handler;

import cn.hutool.json.JSONUtil;
import com.passion.practice.springbootsecurity.common.enums.ResultCode;
import com.passion.practice.springbootsecurity.common.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未登录或者登录过期
 * 自定义返回结果
 *
 * @author: lsl
 * @date: 2020-07-10
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        LOGGER.info("未登录 CustomAuthenticationEntryPoint");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        response.getWriter().println(JSONUtil.parse(ResultUtil.fail(ResultCode.USER_NOT_LOGIN)));
        response.getWriter().flush();
    }
}
