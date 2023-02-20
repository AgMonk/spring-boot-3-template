package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.response.Res;
import org.jetbrains.annotations.NotNull;

/**
 * 抽象日志策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/20 17:02
 */
public abstract class AbstractLogStrategy implements OperationLogStrategy {
    /**
     * 获取关联实体类型
     * @param context 上下文
     * @return 关联实体类型
     */
    @NotNull
    @Override
    public Class<?> getEntityClass(OperationLogContext context) {
        //尝试从返回结果获取 EntityClass
        final Class<?> fromResult = OperationLogStrategy.findEntityClassFromResult(context.result());
        if (fromResult != null) {
            return fromResult;
        }
        //尝试从请求参数中获取 EntityClass
        final Class<?> fromParam = OperationLogStrategy.findEntityClassFromParam(context.paramArgs());
        if (fromParam != null) {
            return fromParam;
        }
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
        // 如果返回对象为 Res 类型 且 data 字段为 Vo 类型
        if (context.result() instanceof Res<?> res && res.getData() instanceof BaseVo vo) {
            return vo.getId();
        }
        // 表示未知
        return -1L;
    }
}
