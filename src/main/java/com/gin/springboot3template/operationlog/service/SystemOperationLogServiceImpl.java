package com.gin.springboot3template.operationlog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.operationlog.dao.SystemOperationLogDao;
import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:02
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemOperationLogServiceImpl extends ServiceImpl<SystemOperationLogDao, SystemOperationLog> implements SystemOperationLogService {
}