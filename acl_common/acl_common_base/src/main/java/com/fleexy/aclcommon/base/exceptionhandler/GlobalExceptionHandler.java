package com.fleexy.aclcommon.base.exceptionhandler;

import com.fleexy.aclcommon.base.utils.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody //为了返回数据
    public RespData error(Exception e) {
        log.error(e.getMessage());
        return RespData.error().message("执行了全局异常处理..");
    }

    //特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody //为了返回数据
    public RespData error(ArithmeticException e) {
        log.error(e.getMessage());
        return RespData.error().message("执行了ArithmeticException异常处理..");
    }

    //自定义异常
    @ExceptionHandler(GuliException.class)
    @ResponseBody //为了返回数据
    public RespData error(GuliException e) {
        log.error(e.getMessage());
        return RespData.error().code(e.getCode()).message(e.getMsg());
    }

}
