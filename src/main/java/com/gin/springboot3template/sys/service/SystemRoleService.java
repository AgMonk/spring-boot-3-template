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
    /**
     * 添加角色
     * @param param 参数
     * @return 添加好的角色
     */
    default SystemRole addByParam(SystemRole.Param param) {
        final SystemRole entity = param.build();
        save(entity);
        return entity;
    }

    /**
     * 修改角色
     * @param roleId 角色id
     * @param param  参数
     * @return 修改后的角色
     */
    default SystemRole updateByIdParam(long roleId, SystemRole.Param param) {
        final SystemRole entity = param.build();
        entity.setId(roleId);
        updateById(entity);
        return entity;
    }
}