package com.gin.springboot3template.operationlog.service;

import com.gin.springboot3template.operationlog.entity.SystemOperationLogOld;
import com.gin.springboot3template.sys.service.MyService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/22 15:11
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemOperationLogOldService extends MyService<SystemOperationLogOld> {


}