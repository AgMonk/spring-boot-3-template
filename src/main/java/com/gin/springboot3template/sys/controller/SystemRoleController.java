package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.dto.param.SystemRolePageParam;
import com.gin.springboot3template.sys.response.ResPage;
import com.gin.springboot3template.sys.service.RolePermissionService;
import com.gin.springboot3template.sys.service.SystemRoleService;
import com.gin.springboot3template.sys.vo.SystemRoleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

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
    public static final String API_PREFIX = "/sys/role";
    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;
    /* todo 添加角色 修改角色 删除角色 分页查询角色 查询所有角色 通过关键字检索角色*/

    @GetMapping("page")
    @Operation(summary = "分页查询")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public ResPage<SystemRoleVo> page(@ParameterObject @Validated SystemRolePageParam pageParam, HttpServletRequest request) {
        // todo 添加筛选条件: 持有指定角色的用户
        return systemRoleService.pageByParam(pageParam, SystemRoleVo::new);
    }

}
