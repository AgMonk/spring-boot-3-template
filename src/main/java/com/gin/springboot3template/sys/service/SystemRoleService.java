package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.SystemRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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
    default List<SystemRole> addByParam(Collection<SystemRole.Param> param) {
        final List<SystemRole> roles = param.stream().map(SystemRole.Param::build).toList();
        saveBatch(roles);
        return roles;
    }

    /**
     * 通过名称查询角色
     * @param name 名称
     * @return 角色
     */
    default SystemRole getByName(String name) {
        final QueryWrapper<SystemRole> qw = new QueryWrapper<>();
        qw.eq("name", name);
        return getOne(qw);
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
        entity.setTimeUpdate(System.currentTimeMillis() / 1000);
        updateById(entity);
        return entity;
    }
}