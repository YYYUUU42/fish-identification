package com.cloud.fish.identification.exception;


import com.cloud.fish.common.enums.AppHttpCodeEnum;
import com.cloud.fish.common.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult systemExceptionHandler(CustomException e) {
        //打印异常信息
        log.error("出现了异常！ {}", e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(e.getCode(), e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(Exception e) {
        //打印异常信息
        log.error("出现了异常！ {}", e);
        //从异常对象中获取提示信息封装返回
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
