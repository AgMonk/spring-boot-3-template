package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.DescriptionContext;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 描述生成策略的默认实现
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:38
 */
public abstract class AbstractDescriptionStrategy implements DescriptionStrategy {
    /**
     * 添加策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object add(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 删除策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object del(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 查询策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object query(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        return pjp.proceed();
    }

    /**
     * 修改策略
     * @param pjp     接触点
     * @param context 上下文
     * @return 返回
     * @throws Throwable 异常
     */
    @Override
    public Object update(ProceedingJoinPoint pjp, DescriptionContext context) throws Throwable {
        return pjp.proceed();
    }
}
