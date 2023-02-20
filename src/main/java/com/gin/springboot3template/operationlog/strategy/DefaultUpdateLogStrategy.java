package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 默认更新策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/20 17:35
 */
@Component
@LogStrategy(type = OperationType.UPDATE)
public class DefaultUpdateLogStrategy extends AbstractLogStrategy {
    /**
     * 获取日志描述
     * @param context 上下文
     * @return 日志描述
     */
    @NotNull
    @Override
    public String getDescription(OperationLogContext context) {
        //todo
        return null;
    }
}
