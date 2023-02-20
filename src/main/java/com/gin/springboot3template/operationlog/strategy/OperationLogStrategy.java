package com.gin.springboot3template.operationlog.strategy;

import java.util.List;

/**
 * 日志生成策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 15:51
 */
public interface OperationLogStrategy {
    /**
     * 获取关联实体ID
     * @param result 请求结果
     * @param params 操作参数
     * @return 关联实体ID
     * @throws Throwable 异常
     */
    Long getEntityId(Object result, List<Object> params) throws Throwable;

    /**
     * 获取关联实体类型
     * @param result 请求结果
     * @param params 操作参数
     * @return 关联实体类型
     * @throws Throwable 异常
     */
    Class<?> getEntityClass(Object result, List<Object> params) throws Throwable;

    /**
     * 获取日志描述
     * @param result 请求结果
     * @param params 操作参数
     * @return 日志描述
     * @throws Throwable 异常
     */
    String getDescription(Object result, List<Object> params) throws Throwable;

}
