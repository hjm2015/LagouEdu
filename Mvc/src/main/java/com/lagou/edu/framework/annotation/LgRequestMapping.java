package com.lagou.edu.framework.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LgRequestMapping {
    String value() default "";
}
