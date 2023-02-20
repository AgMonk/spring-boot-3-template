package com.gin.springboot3template.operationlog.service;

import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.sys.service.MyService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:02
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemOperationLogService extends MyService<SystemOperationLog> {

    /**
     * 写入日志
     * @param log 日志
     */
    @Async
    default void write(Collection<SystemOperationLog> log) {
        saveBatch(log);
    }
}