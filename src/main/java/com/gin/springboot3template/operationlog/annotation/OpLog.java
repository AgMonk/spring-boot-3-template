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
     * 被操作的实体类型,将决定日志的生成策略
     */
    Class<?> clazz();

    /**
     * 操作类型
     */
    OperationType type();

    /**
     * Spring-EL 表达式
     */
    String[] expression() default {};
}
