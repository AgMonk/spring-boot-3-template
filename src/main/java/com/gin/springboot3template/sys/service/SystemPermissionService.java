package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.entity.SystemPermission;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemPermissionService extends MyService<SystemPermission> {


}