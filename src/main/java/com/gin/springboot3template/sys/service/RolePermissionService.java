package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.bo.RolePermission;
import com.gin.springboot3template.sys.bo.UserRole;
import com.gin.springboot3template.sys.entity.RelationUserRole;
import com.gin.springboot3template.sys.entity.SystemRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色和权限统一服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 17:10
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RolePermissionService {

    private final SystemRoleService systemRoleService;
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;
    private final RelationUserRoleService relationUserRoleService;

    /**
     * 查询指定用户的角色及其权限
     * @param userId 用户id
     * @return 角色及其权限
     */
    public List<RolePermission> listAuthorityByUserId(long userId) {
        //todo
        return null;
    }

    /**
     * 查询指定用户的角色及其权限
     * @param userId 用户id
     * @return 角色及其权限
     */
    public List<UserRole> listAuthorityByUserId(Collection<Long> userId) {
        //todo
        return null;
    }

    /**
     * 查询指定角色的权限
     * @param roleIds 角色id
     * @return 权限
     */
    public List<RolePermission> listPermissionsByRoleId(Collection<Long> roleIds) {
        //todo
        return null;
    }

    /**
     * 查询指定用户的角色
     * @param userId 用户id
     * @return 角色
     */
    public List<RolePermission> listRolesByUserId(long userId) {
        //todo
        return null;
    }

    /**
     * 查询指定用户的角色
     * @param userId 用户id
     * @return 角色
     */
    public List<UserRole> listRolesByUserId(Collection<Long> userId) {
        //todo
        return null;
    }

    /**
     * 为指定角色添加权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    public void permissionAddByRoleId(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 为指定角色配置权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    public void permissionConfigByRoleId(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 为指定角色删除权限
     * @param roleId  角色id
     * @param permIds 权限id
     */
    public void permissionDelByRoleId(long roleId, Collection<Long> permIds) {
        //todo
    }

    /**
     * 添加角色
     * @param param 参数
     * @return 添加好的角色
     */
    public SystemRole roleAdd(SystemRole.Param param) {
        final SystemRole entity = param.build();
        systemRoleService.save(entity);
        return entity;
    }

    /**
     * 为指定用户添加角色
     * @param userId 用户id
     * @param params 参数
     * @return 添加好的角色
     */
    public List<RelationUserRole> roleAddByUserId(long userId, Collection<RelationUserRole.Param> params) {
        final List<RelationUserRole> userRoles = params.stream().map(i -> i.build(userId)).toList();
        relationUserRoleService.saveBatch(userRoles);
        return userRoles;
    }

    /**
     * 为指定用户配置角色
     * @param userId 用户id
     * @param params 角色id
     */
    public void roleConfigByUserId(long userId, Collection<RelationUserRole.Param> params) {
        // 查询指定用户持有的角色id
        //已有数据 (含有id)
        final List<RelationUserRole> oldData = relationUserRoleService.listByUserId(Collections.singleton(userId));
        //新数据 (不含id)
        final List<RelationUserRole> newData = new ArrayList<>(params.stream().map(i -> i.build(userId)).toList());

        //过滤出不存在的，进行删除
        final List<RelationUserRole> data2Del = oldData.stream().filter(o -> !newData.contains(o)).toList();
        if (data2Del.size() > 0) {
            relationUserRoleService.removeBatchByIds(data2Del.stream().map(RelationUserRole::getRoleId).collect(Collectors.toList()));
            oldData.removeAll(data2Del);
        }

        //过滤出新增的，进行添加
        final List<RelationUserRole> data2Add = newData.stream().filter(o -> !oldData.contains(o)).toList();
        if (data2Add.size() > 0) {
            relationUserRoleService.saveBatch(data2Add);
            newData.removeAll(data2Add);
        }

        //过滤出已经存在的，进行修改
        if (newData.size() > 0) {
            final long now = System.currentTimeMillis() / 1000;
            final List<RelationUserRole> data2Update = newData.stream().peek(nd -> {
                // 补充id
                nd.setId(oldData.stream().filter(od -> od.equals(nd)).toList().get(0).getId());
                // 设置修改时间
                nd.setTimeUpdate(now);
            }).toList();
            relationUserRoleService.updateBatchById(data2Update);
        }

    }

    /**
     * 删除角色(连带删除所有对该角色的持有)
     * @param roleId 角色id
     * @return 被删除的角色
     */
    public List<SystemRole> roleDel(Collection<Long> roleId) {
        if (CollectionUtils.isEmpty(roleId)) {
            return new ArrayList<>();
        }
        //删除角色
        final List<SystemRole> systemRoles = systemRoleService.listByIds(roleId);
        systemRoleService.removeByIds(roleId);
        //连带删除所有对该角色的持有
        final QueryWrapper<RelationUserRole> qw = new QueryWrapper<>();
        qw.in("role_id", roleId);
        relationUserRoleService.remove(qw);
        return systemRoles;
    }

    /**
     * 为指定用户删除角色
     * @param userId  用户id
     * @param roleIds 角色id
     */
    public void roleDelByUserId(long userId, Collection<Long> roleIds) {
        final QueryWrapper<RelationUserRole> qw = new QueryWrapper<>();
        qw.in("role_id", roleIds).eq("user_id", userId);
        relationUserRoleService.remove(qw);
    }

    /**
     * 修改角色
     * @param roleId 角色id
     * @param param  参数
     * @return 修改后的角色
     */
    public SystemRole roleUpdate(long roleId, SystemRole.Param param) {
        final SystemRole entity = param.build();
        entity.setId(roleId);
        systemRoleService.updateById(entity);
        return entity;
    }

}
