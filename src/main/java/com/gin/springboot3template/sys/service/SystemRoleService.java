package com.gin.springboot3template.sys.service;

import com.gin.springboot3template.sys.entity.SystemRole;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:57
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemRoleService extends MyService<SystemRole> {

}