package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.dto.RegForm;
import com.gin.springboot3template.sys.entity.RelationUserRole;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.service.*;
import com.gin.springboot3template.sys.service.impl.SystemUserServiceImpl;
import com.gin.springboot3template.sys.validation.EntityId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

import static com.gin.springboot3template.sys.bo.Constant.MESSAGE_NOT_CONFIG_ADMIN;

/**
 * 用户接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/13 10:28
 */
@MyRestController(SystemUserAdminController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = "用户管理接口")
@Slf4j
public class SystemUserAdminController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = SystemUserController.API_PREFIX + "/admin";
    private final SystemUserService systemUserService;
    private final SystemUserInfoService systemUserInfoService;
    private final RolePermissionService rolePermissionService;
    private final RelationUserRoleService relationUserRoleService;
    private final SystemRoleService systemRoleService;

    @PostMapping("create")
    @Operation(summary = "创建用户")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUser.Vo> create(@RequestBody @Validated RegForm regForm) {
        return Res.of(new SystemUser.Vo(systemUserService.reg(regForm)), "创建成功");
    }

    @PostMapping("lock")
    @Operation(summary = "锁定/解锁指定用户", description = "锁定用户不能登陆<br/>" + MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void lock(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        rolePermissionService.forbiddenConfigAdminUser(userId);

        //todo

    }

    @GetMapping("page")
    @Operation(summary = "分页查询用户账号信息")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void page(HttpServletRequest request) {
        //todo
    }

    @PostMapping("resetPassword")
    @Operation(summary = "重置用户的密码", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void reset(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        rolePermissionService.forbiddenConfigAdminUser(userId);

        //todo
    }

    @PostMapping("roleAdd")
    @Operation(summary = "为指定用户添加角色", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<RelationUserRole>> roleAdd(HttpServletRequest request, @EntityId(service = SystemUserServiceImpl.class) @RequestParam Long userId, @RequestBody @Validated Collection<RelationUserRole.Param> params) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        systemRoleService.validateRoleId(params.stream().map(RelationUserRole.Param::getRoleId).toList());
        final List<RelationUserRole> roleList = relationUserRoleService.add(userId, params);
        return Res.of(roleList);
    }

    @PostMapping("roleConfig")
    @Operation(summary = "为指定用户配置角色", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<RelationUserRole>> roleConfig(HttpServletRequest request, @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, @RequestBody @Validated Collection<RelationUserRole.Param> params) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        systemRoleService.validateRoleId(params.stream().map(RelationUserRole.Param::getRoleId).toList());
        final List<RelationUserRole> roleList = relationUserRoleService.config(userId, params);
        return Res.of(roleList);
    }

    @PostMapping("roleDel")
    @Operation(summary = "为指定用户删除角色", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Void> roleDel(HttpServletRequest request, @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, @RequestBody @Validated List<Long> roleId) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        relationUserRoleService.del(userId, roleId);
        return Res.of(null);
    }

    @GetMapping("roleList")
    @Operation(summary = "查询用户持有的角色")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void roleList(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
//todo
    }

    @GetMapping("userInfoFind")
    @Operation(summary = "查询指定用户的个人信息")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserInfo.Vo> userInfoFind(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "未找到用户个人信息,请先录入");
        }
        final SystemUserInfo.Vo vo = new SystemUserInfo.Vo(userInfo);
        return Res.of(vo);
    }

    @PostMapping("userInfoUpdate")
    @Operation(summary = "修改指定用户的个人信息", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Object> userInfoUpdate(HttpServletRequest request, @RequestBody @Validated SystemUserInfo.Param param, @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId) {
        rolePermissionService.forbiddenConfigAdminUser(userId);


        systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(null, "修改成功");
    }

    private void validatedNotAdmin(Collection<Long> roleId) {
        systemRoleService.forbiddenConfigAdminRole(roleId);
    }
}
