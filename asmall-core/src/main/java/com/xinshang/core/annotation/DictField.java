package com.xinshang.core.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DictField {

    /**
     * 字典类型的code
     * @return
     */
    String value() default "";

    /**
     * 描述/备注
     * @return
     */
    String description() default "no description";

}
