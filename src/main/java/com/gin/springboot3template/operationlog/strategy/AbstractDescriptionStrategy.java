package com.gin.springboot3template.operationlog.strategy;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 抽象描述策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/21 14:36
 */
@Slf4j
public abstract class AbstractDescriptionStrategy implements DescriptionStrategy {

    /**
     * 查询操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    public abstract String query(OperationLogContext context);

    /**
     * 更新操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    public abstract String update(OperationLogContext context);

    /**
     * 删除操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    public abstract String del(OperationLogContext context);

    /**
     * 添加操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    public abstract String add(OperationLogContext context);

    /**
     * 上传操作的生成描述策略
     * @param context 上下文
     * @return 描述
     */
    public abstract String upload(OperationLogContext context);

    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        Class<?> entityClass = this.getClass().getAnnotation(LogStrategy.class).value();
        if (!entityClass.equals(context.entityClass())) {
            log.debug("非专用描述策略, 策略:{} , 实体:{}", entityClass, context.entityClass());
        }
        //noinspection AlibabaSwitchStatement
        switch (context.type()) {
            case ADD -> {
                return add(context);
            }
            case DEL -> {
                return del(context);
            }
            case UPDATE -> {
                return update(context);
            }
            case QUERY -> {
                return query(context);
            }
            case UPLOAD -> {
                return upload(context);
            }
            default -> {
                log.warn("未匹配的操作类型: " + context.type());
                return null;
            }
        }
    }
}
