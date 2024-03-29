package com.gin.springboot3template.sys.annotation;

import com.gin.springboot3template.sys.config.JsonResponseWrapper;

import java.lang.annotation.*;

/**
 * 是否禁用{@link JsonResponseWrapper}的自动包装
 * @author bx002
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestWrapper {
    /**
     * 禁用包装
     * @return 是否禁用
     */
    boolean disable() default true;
}