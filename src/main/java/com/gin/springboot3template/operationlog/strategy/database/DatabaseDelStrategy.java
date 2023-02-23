package com.gin.springboot3template.operationlog.strategy.database;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.strategy.DescriptionStrategy;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;

/**
 * 数据库备份删除策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 10:26
 */
@Component
@LogStrategy(value = Database.class, type = OperationType.DEL)
public class DatabaseDelStrategy implements DescriptionStrategy {
    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        //todo
        return null;
    }
}
