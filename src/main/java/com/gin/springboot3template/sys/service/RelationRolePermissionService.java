package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.RelationRolePermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */

@Transactional(rollbackFor = Exception.class)
public interface RelationRolePermissionService extends MyService<RelationRolePermission> {

    /**
     * 为指定角色添加权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    default void add(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 为指定角色配置权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    default void config(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 为指定角色删除权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    default void del(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 根据角色Id 查询其持有的权限
     * @param roleId 角色id
     * @return 权限
     */
    default List<RelationRolePermission> listByRoleId(Collection<Long> roleId) {
        final QueryWrapper<RelationRolePermission> qw = new QueryWrapper<>();
        qw.in("role_id", roleId);
        return list(qw);
    }
}