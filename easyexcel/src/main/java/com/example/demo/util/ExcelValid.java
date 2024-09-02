package com.example.demo.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ExcelValid验证注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelValid {

    // 字段名
    String filedName();

    // 导入唯一性验证（多个字段则取联合验证）
    boolean unique() default false;

    // 字段不为null
    boolean notEmpty() default false;

    // 单字段唯一性校验
    String singleUnique() default "";

    // 校验格式 选项 phone、email、number
    String format() default "";

    // 校验日期格式 yyyy-MM-dd HH:mm:ss yyyyMMdd
    String dateFormat() default "";
}
