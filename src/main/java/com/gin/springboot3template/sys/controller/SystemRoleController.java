package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.dto.form.SystemRoleDelForm;
import com.gin.springboot3template.sys.dto.form.SystemRoleForm;
import com.gin.springboot3template.sys.dto.param.SystemRolePageParam;
import com.gin.springboot3template.sys.entity.SystemRole;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.response.ResPage;
import com.gin.springboot3template.sys.service.RelationRolePermissionService;
import com.gin.springboot3template.sys.service.RolePermissionService;
import com.gin.springboot3template.sys.service.SystemRoleService;
import com.gin.springboot3template.sys.service.impl.SystemRoleServiceImpl;
import com.gin.springboot3template.sys.validation.EntityId;
import com.gin.springboot3template.sys.vo.SystemRoleVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/16 14:11
 */
@MyRestController(SystemRoleController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = "角色管理接口")
@Slf4j
public class SystemRoleController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/sys/role";
    private final SystemRoleService systemRoleService;
    private final RolePermissionService rolePermissionService;
    private final RelationRolePermissionService relationRolePermissionService;

    @PostMapping("add")
    @Operation(summary = "添加角色", description = "返回添加完成的角色")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> add(
            @RequestBody @Validated @NotEmpty @Parameter(description = "角色列表") List<SystemRoleForm> roles,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final List<SystemRoleVo> data = systemRoleService.addByParam(roles).stream().map(SystemRoleVo::new).collect(Collectors.toList());
        return Res.of(data);
    }

    @PostMapping("del")
    @Operation(summary = "删除角色", description = "注意:将连带删除所有对该角色的持有")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> del(@RequestBody @Validated SystemRoleDelForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final List<Long> roleId = form.getRoleId();
        systemRoleService.validateRoleId(roleId);
        final List<SystemRoleVo> res = rolePermissionService.roleDel(roleId).stream().map(SystemRoleVo::new).toList();
        return Res.of(res, "删除成功");
    }

    @GetMapping("page")
    @Operation(summary = "分页查询角色")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public ResPage<SystemRoleVo> page(
            @ParameterObject @Validated SystemRolePageParam pageParam,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return systemRoleService.pageByParam(pageParam, SystemRoleVo::new);
    }

    @PostMapping("update")
    @Operation(summary = "修改角色", description = "返回修改完成的角色")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRoleVo> update(
            @RequestBody @Validated SystemRoleForm param,
            @RequestParam @EntityId(service = SystemRoleServiceImpl.class) @Parameter(description = "角色id") Long roleId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final SystemRole systemRole = systemRoleService.updateByIdParam(roleId, param);
        final SystemRoleVo vo = new SystemRoleVo(systemRole);
        return Res.of(vo);
    }

    // todo 为角色: 添加权限 / 移除权限 / 配置权限
}
