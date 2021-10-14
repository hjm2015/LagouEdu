package com.lagou.edu.framework.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LgAutowired {
    String value() default "";
}
