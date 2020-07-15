package com.passion.practice.springbootsecurity.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: production
 * @author: lsl
 * @date: 2020-07-08
 */
@Controller
@ResponseBody
@RequestMapping("/product")
public class ProductController {

    @RequestMapping("/info")
    public String productInfo() {
        String currentUser = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            currentUser = ((UserDetails) principal).getUsername();
        } else {
            currentUser = principal.toString();
        }
        return "some product info, and current user is: " + currentUser;
    }

}
