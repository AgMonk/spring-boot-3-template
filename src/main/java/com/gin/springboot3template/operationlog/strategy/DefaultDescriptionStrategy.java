package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import org.springframework.stereotype.Component;

/**
 * 默认描述策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/21 14:44
 */
@Component
@LogStrategy
public class DefaultDescriptionStrategy extends AbstractDescriptionStrategy {

    /**
     * 添加操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String add(OperationLogContext context) {
        return null;
    }

    /**
     * 删除操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String del(OperationLogContext context) {
        return null;
    }

    /**
     * 查询操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String query(OperationLogContext context) {
        return null;
    }

    /**
     * 更新操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String update(OperationLogContext context) {
        return null;
    }

    /**
     * 上传操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String upload(OperationLogContext context) {
        return null;
    }
}
