package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色和权限联合服务
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
    private final SystemUserService systemUserService;
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;
    private final RelationUserRoleService relationUserRoleService;


    /**
     * 查询指定用户的角色及其权限
     * @param userId 用户id
     * @return 角色及其权限
     */
    public List<SystemUser.Bo> listAuthorityByUserId(Collection<Long> userId) {
        //查询用户列表
        final List<SystemUser.Bo> userData = systemUserService.listByIds(userId).stream().map(SystemUser.Bo::new).toList();

        //查询用户持有的角色
        final List<RelationUserRole.Bo> userRoles = relationUserRoleService.listByUserId(userId).stream().map(RelationUserRole.Bo::new).toList();
        // 如果没有角色 直接返回
        if (userRoles.size() == 0) {
            return userData;
        }
        //将角色放入用户bo
        userData.forEach(u -> u.setRoles(userRoles.stream().filter(i -> i.getUserId().equals(u.getId())).collect(Collectors.toList())));

        // 角色id去重
        final List<Long> roleId = userRoles.stream().map(RelationUserRole.Bo::getRoleId).distinct().sorted().toList();
        // 查询角色信息
        final List<SystemRole> systemRoles = systemRoleService.listByIds(roleId);
        // 构建map
        HashMap<Long, SystemRole> roleHashMap = new HashMap<>(systemRoles.size());
        systemRoles.forEach(i -> roleHashMap.put(i.getId(), i));
        //补充角色三个字段信息
        userRoles.forEach(i -> i.with(roleHashMap.get(i.getRoleId())));

        // 角色持有的权限信息
        final List<RelationRolePermission> rolePermissions = relationRolePermissionService.listByRoleId(roleId);
        // 如果没有权限 直接返回
        if (rolePermissions.size() == 0) {
            return userData;
        }
        // 权限id去重
        final List<Long> permissionId = rolePermissions.stream().map(RelationRolePermission::getPermissionId).distinct().sorted().toList();
        // 权限列表
        final List<SystemPermission> permissions = systemPermissionService.listByIds(permissionId);
        //为每个角色补充权限信息
        userRoles.forEach(role -> {
            // 该角色持有的权限id
            final List<Long> permId = rolePermissions.stream().filter(i -> i.getRoleId().equals(role.getRoleId())).map(RelationRolePermission::getPermissionId).toList();
            // 放入权限
            role.setPermissions(permissions.stream().filter(i -> permId.contains(i.getId())).collect(Collectors.toList()));
        });
        return userData;
    }


    /**
     * 查询指定用户的角色
     * @param userId 用户id
     * @return 角色
     */
    public List<SystemUser.Bo> listUserRole(Collection<Long> userId) {
        //todo
        final List<SystemUser.Bo> data = systemUserService.listByIds(userId).stream().map(SystemUser.Bo::new).toList();

        final List<RelationUserRole> relationUserRoles = relationUserRoleService.listByUserId(userId);
        return data;
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

}
