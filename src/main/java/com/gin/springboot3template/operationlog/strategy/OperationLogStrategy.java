package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import org.jetbrains.annotations.NotNull;

/**
 * 日志生成策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 15:51
 */
public interface OperationLogStrategy {
    /**
     * 获取关联实体ID
     * @param context 上下文
     * @return 关联实体ID
     */
    @NotNull
    Long getEntityId(OperationLogContext context);

    /**
     * 获取关联实体类型
     * @param context 上下文
     * @return 关联实体类型
     */
    @NotNull
    Class<?> getEntityClass(OperationLogContext context);

    /**
     * 获取日志描述
     * @param context 上下文
     * @return 日志描述
     */
    @NotNull
    String getDescription(OperationLogContext context);

}
