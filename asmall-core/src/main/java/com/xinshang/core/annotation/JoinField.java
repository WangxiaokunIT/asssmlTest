package com.xinshang.core.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JoinField {

    /**
     * 要关联的表名
     * @return
     */
    String tableName() default "";

    /**
     * 要关联的字段名
     * @return
     */
    String relationColumn() default "";

    /**
     * 取出的字段名
     * @return
     */
    String targetColumn() default "";

    /**
     * 备注/描述
     * @return
     */
    String description() default "no description";

}
