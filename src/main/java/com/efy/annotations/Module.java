package com.efy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author : Efy Shu
 * Date : 2021/5/11 14:12
 * Description :
 * 模块注解,用来标记模块,打印输入输出日志等功能
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Module {
    /**模块名 冗余name*/
    String value() default "";
    /**模块名 冗余value*/
    String name() default "";
    /**模块标签,会以【tag1】【tag2】形式添加在name前*/
    String[] tags() default {};
}
