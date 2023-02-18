package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.DescriptionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 描述生成策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 15:51
 */
public interface DescriptionStrategy {
    /**
     * 被操作的实体类型
     * @return 类对象
     */
    Class<?> clazz();

    /**
     * 添加策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    Object add(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable;

    /**
     * 删除策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    Object del(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable;

    /**
     * 修改策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    Object update(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable;

    /**
     * 查询策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    Object query(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable;
}   
