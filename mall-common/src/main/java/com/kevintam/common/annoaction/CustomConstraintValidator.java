package com.kevintam.common.annoaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;

/**
 * @author kevintam
 * @version 1.0
 * @title
 * @description
 * @createDate 2022/6/21
 */
@Slf4j
public class CustomConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private HashSet<Integer> set = new HashSet<>();
    /**
     * 初始化方法
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] value = constraintAnnotation.value();//传入的参数
            for (int val : value) {
                set.add(val);
            }
    }

    /**
     * 判断是否校验成功
     *
     * @param value 就是我们需要校验的值
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        return set.contains(value);
    }
}
