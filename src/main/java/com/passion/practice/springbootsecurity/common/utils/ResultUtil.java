package com.passion.practice.springbootsecurity.common.utils;

import com.passion.practice.springbootsecurity.common.entity.JsonResult;
import com.passion.practice.springbootsecurity.common.enums.ResultCode;

/**
 * @description:
 * @author: lsl
 * @date: 2020-07-08
 */
public class ResultUtil {
    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult(true, data);
    }

    public static JsonResult fail() {
        return new JsonResult(false);
    }

    public static JsonResult fail(ResultCode resultEnum) {
        return new JsonResult(false, resultEnum);
    }
}
