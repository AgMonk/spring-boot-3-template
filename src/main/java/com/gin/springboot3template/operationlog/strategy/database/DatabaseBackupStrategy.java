package com.gin.springboot3template.operationlog.strategy.database;

import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.strategy.DescriptionStrategy;
import com.gin.springboot3template.sys.vo.FileInfo;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据库备份策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/23 10:26
 */
@Component
@LogStrategy(value = Database.class, type = OperationType.BACKUP)
public class DatabaseBackupStrategy implements DescriptionStrategy {
    /**
     * 生成描述
     * @param context 上下文
     * @return 描述
     */
    @Override
    public String generateDescription(OperationLogContext context) {
        final List<Object> sufExp = context.sufExp();
        if (sufExp.size() > 0 && sufExp.get(0) instanceof FileInfo fileInfo) {
            return fileInfo.toString();
        }
        return null;
    }
}
