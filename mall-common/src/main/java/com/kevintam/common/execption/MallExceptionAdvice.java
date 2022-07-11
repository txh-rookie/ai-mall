package com.kevintam.common.execption;

import com.kevintam.common.execption.CustomExceptionEnum;
import com.kevintam.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/21
 * 顶一个统一的异常类
 */
@Slf4j
@RestControllerAdvice
public class MallExceptionAdvice {

    //定义一个异常类
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R checkException(MethodArgumentNotValidException e){
       log.error("数据校验出现问题：{},出现的异常类型为:{}",e.getMessage(),e.getClass());
       HashMap<String, String> maps=new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(item->{
            String defaultMessage = item.getDefaultMessage();
            String field = item.getField();
            maps.put(field,defaultMessage);
        });
        return R.error(CustomExceptionEnum.CHECK_EXCEPTION.getCode(),CustomExceptionEnum.CHECK_EXCEPTION.getMessage()).put("data",maps);
    }
    //自定义一个不知道异常的异常处理
    @ExceptionHandler(Throwable.class)
    public R throwException(Exception e){
        return R.error(CustomExceptionEnum.UNKNOWN_EXCEPTION.getCode(), CustomExceptionEnum.CHECK_EXCEPTION.getMessage());
    }
}
