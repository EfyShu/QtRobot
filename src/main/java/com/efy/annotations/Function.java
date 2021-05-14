package com.efy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author : Efy Shu
 * Date : 2021/5/13 22:10
 * Description :
 * 标记哪些类是按钮功能类
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Function {
}
