package com.gin.springboot3template.operationlog.annotation;

import com.gin.springboot3template.operationlog.enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志生成策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogStrategy {
    /**
     * 被操作的实体类型,将决定日志的生成策略
     */
    Class<?> clazz() default Object.class;

    /**
     * 关联的实体类型,日志将以该类型名义写入日志,取默认时将根据日志策略的逻辑获取
     */
    Class<?> entityClass() default Object.class;

    /**
     * 适配的操作类型
     */
    OperationType type();

}
