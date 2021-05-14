package com.efy.annotations;

import java.lang.annotation.*;

/**
 * Author : Efy Shu
 * Date : 2021/5/14 3:12
 * Description :
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Aspect {
    //给哪个注解注册切面
    Class<? extends Annotation> value() default Module.class;
}
