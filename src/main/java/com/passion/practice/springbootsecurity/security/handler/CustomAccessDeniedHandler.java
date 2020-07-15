package com.passion.practice.springbootsecurity.security.handler;

import cn.hutool.json.JSONUtil;
import com.passion.practice.springbootsecurity.common.enums.ResultCode;
import com.passion.practice.springbootsecurity.common.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义 没有权限处理逻辑
 *
 * @author: lsl
 * @date: 2020-07-09
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        LOGGER.info("调用自定义没有权限处理逻辑 CustomAccessDeniedHandler");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JSONUtil.parse(ResultUtil.fail(ResultCode.NO_PERMISSION)));
        response.getWriter().flush();
    }
}
