package com.example.demo.utils;

import com.example.demo.result.Result;
import com.example.demo.result.ResultCode;

public class ResultUtil {
    public static Result buildResult(int resultCode, String message, Object data) {
        return new Result(resultCode, message, data);
    }

    public static Result buildResult(ResultCode resultCode, String message, Object data) {
        return buildResult(resultCode.getCode(), message, data);
    }

    public static Result buildSuccessResult(String message, Object data) {
        return buildResult(ResultCode.SUCCESS, message, data);
    }

    public static Result buildFailResult(String message, Object data) {
        return buildResult(ResultCode.FAIL, message, data);
    }

}
