package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.dto.form.SystemRoleDelForm;
import com.gin.springboot3template.sys.dto.form.SystemRoleForm;
import com.gin.springboot3template.sys.dto.form.SystemRolePermissionForm;
import com.gin.springboot3template.sys.dto.param.SystemRolePageParam;
import com.gin.springboot3template.sys.entity.SystemPermission;
import com.gin.springboot3template.sys.entity.SystemRole;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.response.ResPage;
import com.gin.springboot3template.sys.service.RelationRolePermissionService;
import com.gin.springboot3template.sys.service.RolePermissionService;
import com.gin.springboot3template.sys.service.SystemPermissionService;
import com.gin.springboot3template.sys.service.SystemRoleService;
import com.gin.springboot3template.sys.service.impl.SystemRoleServiceImpl;
import com.gin.springboot3template.sys.validation.EntityId;
import com.gin.springboot3template.sys.vo.SystemRolePermissionVo;
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

import java.util.Collections;
import java.util.HashMap;
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
    private final SystemPermissionService systemPermissionService;

    @PostMapping("permissionAdd")
    @Operation(summary = "为角色添加权限", description = "返回添加的角色权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRolePermissionVo> permissionAdd(
            @RequestBody @Validated SystemRolePermissionForm form,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final List<Long> permIds = form.getPermIds();
        //校验权限id
        systemPermissionService.validatePermId(permIds);
        //添加权限
        return Res.of(SystemRolePermissionVo.of(form.getRoleId(), relationRolePermissionService.add(form.getRoleId(), permIds)));
    }

    @PostMapping("permissionConfig")
    @Operation(summary = "为角色配置权限", description = "返回配置的角色权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRolePermissionVo> permissionConfig(
            @RequestBody @Validated SystemRolePermissionForm form,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final List<Long> permIds = form.getPermIds();
        //校验权限id
        systemPermissionService.validatePermId(permIds);
        //移除权限
        return Res.of(SystemRolePermissionVo.of(form.getRoleId(), relationRolePermissionService.config(form.getRoleId(), permIds)));
    }

    @PostMapping("permissionDel")
    @Operation(summary = "为角色移除权限", description = "返回移除的角色权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRolePermissionVo> permissionDel(
            @RequestBody @Validated SystemRolePermissionForm form,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final List<Long> permIds = form.getPermIds();
        //校验权限id
        systemPermissionService.validatePermId(permIds);
        //移除权限
        return Res.of(SystemRolePermissionVo.of(form.getRoleId(), relationRolePermissionService.del(form.getRoleId(), permIds)));
    }

    @GetMapping("permissionList")
    @Operation(summary = "查询角色持有的权限")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemPermission>> permissionList(
            @RequestParam @EntityId(service = SystemRoleServiceImpl.class) @Parameter(description = "角色id") Long roleId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final HashMap<Long, List<SystemPermission>> map = rolePermissionService.findRolePermissionMap(Collections.singleton(roleId));
        return Res.of(map.get(roleId));
    }

    @PostMapping(Constant.Api.ADD)
    @Operation(summary = "添加角色", description = "返回添加完成的角色")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> roleAdd(
            @RequestBody @Validated @NotEmpty @Parameter(description = "角色列表") List<SystemRoleForm> roles,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final List<SystemRoleVo> data = systemRoleService.addByParam(roles).stream().map(SystemRoleVo::new).collect(Collectors.toList());
        return Res.of(data);
    }

    @PostMapping(Constant.Api.DEL)
    @Operation(summary = "删除角色", description = "注意:将连带删除所有对该角色的持有")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemRoleVo>> roleDel(@RequestBody @Validated SystemRoleDelForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final List<Long> roleId = form.getRoleId();
        systemRoleService.validateRoleId(roleId);
        final List<SystemRoleVo> res = rolePermissionService.roleDel(roleId).stream().map(SystemRoleVo::new).toList();
        return Res.of(res, "删除成功");
    }

    @GetMapping(Constant.Api.PAGE)
    @Operation(summary = "分页查询角色")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public ResPage<SystemRoleVo> rolePage(
            @ParameterObject SystemRolePageParam pageParam,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return systemRoleService.pageByParam(pageParam, SystemRoleVo::new);
    }

    @PostMapping(Constant.Api.UPDATE)
    @Operation(summary = "修改角色", description = "返回修改完成的角色")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemRoleVo> roleUpdate(
            @RequestBody @Validated SystemRoleForm param,
            @RequestParam @EntityId(service = SystemRoleServiceImpl.class) @Parameter(description = "角色id") Long roleId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final SystemRole systemRole = systemRoleService.updateByIdParam(roleId, param);
        final SystemRoleVo vo = new SystemRoleVo(systemRole);
        return Res.of(vo);
    }
}
