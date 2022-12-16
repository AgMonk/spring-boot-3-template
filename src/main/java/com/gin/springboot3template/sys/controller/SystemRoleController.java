package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.service.RolePermissionService;
import com.gin.springboot3template.sys.service.SystemRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 角色接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 14:11
 */
@MyRestController(SystemRoleController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = "角色接口")
@Slf4j
public class SystemRoleController {
    /**
     * 接口路径前缀
     */
    protected static final String API_PREFIX = "/sys/role";

    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;
    /*todo 添加角色 修改角色 删除角色 分页查询角色 查询所有角色 通过关键字检索角色*/


}
