package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.response.Res;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 默认添加策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 16:38
 */
@Component
@LogStrategy(type = OperationType.ADD)
public class DefaultAddLogStrategy implements OperationLogStrategy {


    /**
     * 获取日志描述
     * @param context 上下文
     * @return 日志描述
     */
    @NotNull
    @Override
    public String getDescription(OperationLogContext context) {
        return null;
    }

    /**
     * 获取关联实体类型
     * @param context 上下文
     * @return 关联实体类型
     */
    @NotNull
    @Override
    public Class<?> getEntityClass(OperationLogContext context) {
        final LogStrategy annotation = this.getClass().getAnnotation(LogStrategy.class);
        if (annotation == null) {
            // 表示未知
            return Object.class;
        }
        // 如果配置了 entityClass 以它为准
        if (!annotation.entityClass().equals(Object.class)) {
            return annotation.entityClass();
        }
        //todo  如果未配置 entityClass 尝试从其他地方获取


        return Object.class;
    }

    /**
     * 获取关联实体ID
     * @param context 上下文
     * @return 关联实体ID
     */
    @NotNull
    @Override
    public Long getEntityId(OperationLogContext context) {
        // 如果返回对象为 Res 类型
        if (context.result() instanceof Res<?> res) {
            // 如果data 字段为 Vo 类型
            if (res.getData() instanceof BaseVo vo) {
                return vo.getId();
            }
        }
        // 表示未知
        return -1L;
    }
}
