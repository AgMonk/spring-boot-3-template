package com.gin.springboot3template.operationlog.annotation;

import com.gin.springboot3template.operationlog.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要记录操作日志
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpLog {
    /**
     * 主实体类型
     */
    Class<?> mainClass();

    /**
     * 主实体ID  Spring-EL 表达式
     */
    String mainId();

    /**
     * 副实体类型
     */
    Class<?> subClass() default Object.class;

    /**
     * 副实体ID  Spring-EL 表达式
     */
    String subId() default "";

    /**
     * 操作类型
     */
    OperationType type();

    /**
     * Spring-EL 表达式
     */
    String[] expression() default {};
}
