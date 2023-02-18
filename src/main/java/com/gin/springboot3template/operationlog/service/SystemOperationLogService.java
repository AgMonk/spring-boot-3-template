package com.gin.springboot3template.operationlog.service;

import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.sys.service.MyService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:02
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemOperationLogService extends MyService<SystemOperationLog> {


}