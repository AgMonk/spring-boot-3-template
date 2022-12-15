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
import java.util.List;

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
