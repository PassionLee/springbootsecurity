package com.passion.practice.springbootsecurity.controller;

import com.passion.practice.springbootsecurity.common.entity.JsonResult;
import com.passion.practice.springbootsecurity.common.utils.ResultUtil;
import com.passion.practice.springbootsecurity.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: admin
 * @author: lsl
 * @date: 2020-07-08
 */
@Controller
@ResponseBody
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/home")
    public String home() {
        return "admin home page";
    }

    @Autowired
    SysUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResult login(@RequestParam String username, @RequestParam String password) {
        String token = userService.login(username, password);
        return ResultUtil.success(token);
    }

}
