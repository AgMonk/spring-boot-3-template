package com.gin.springboot3template.sys.service.impl;

import com.gin.springboot3template.sys.service.RelationRolePermissionService;
import com.gin.springboot3template.sys.service.RelationUserRoleService;
import com.gin.springboot3template.sys.service.SystemPermissionService;
import com.gin.springboot3template.sys.service.SystemRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    // todo 查询指定用户的角色及其权限
    // todo 查询指定角色的权限
    // todo 添加、修改、删除角色
    // todo 为指定用户添加、修改、删除角色
    // todo 为指定角色添加、删除权限

    private final SystemRoleService systemRoleService;
    private final SystemPermissionService systemPermissionService;
    private final RelationRolePermissionService relationRolePermissionService;
    private final RelationUserRoleService relationUserRoleService;

    
}
