package com.gin.springboot3template.sys.service.impl;

import com.gin.springboot3template.sys.security.bo.RolePermission;
import com.gin.springboot3template.sys.security.bo.UserRole;
import com.gin.springboot3template.sys.service.RelationRolePermissionService;
import com.gin.springboot3template.sys.service.RelationUserRoleService;
import com.gin.springboot3template.sys.service.SystemPermissionService;
import com.gin.springboot3template.sys.service.SystemRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * 角色和权限统一服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 17:10
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionService {
    // todo 添加、修改、删除角色


    // todo 查询指定用户的角色
    // todo 为指定角色添加、删除权限
    // todo 为指定用户添加、修改、删除角色

    private final SystemRoleService systemRoleService;
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;
    private final RelationUserRoleService relationUserRoleService;

    /**
     * 查询指定角色的权限
     * @param roleIds 角色id
     * @return 权限
     */
    public List<RolePermission> listPermissionByRoleId(Collection<Long> roleIds) {
        //todo
        return null;
    }

    /**
     * 查询指定用户的角色
     * @param userIds 用户id
     * @return 角色
     */
    public List<UserRole> listRoleByUserId(Collection<Long> userIds) {
        //todo
        return null;
    }
}
