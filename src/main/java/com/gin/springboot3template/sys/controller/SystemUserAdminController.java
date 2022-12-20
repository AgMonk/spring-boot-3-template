package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.bo.SystemUserBo;
import com.gin.springboot3template.sys.dto.form.*;
import com.gin.springboot3template.sys.dto.param.SystemUserPageParam;
import com.gin.springboot3template.sys.entity.RelationUserRole;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.response.ResPage;
import com.gin.springboot3template.sys.service.*;
import com.gin.springboot3template.sys.service.impl.SystemUserServiceImpl;
import com.gin.springboot3template.sys.validation.EntityId;
import com.gin.springboot3template.sys.vo.SystemUserInfoVo;
import com.gin.springboot3template.sys.vo.SystemUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.gin.springboot3template.sys.bo.Constant.Messages.NOT_CONFIG_ADMIN;

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
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserVo> create(@RequestBody @Validated RegForm regForm) {
        return Res.of(new SystemUserVo(systemUserService.reg(regForm)), "创建成功");
    }

    @PostMapping("lock")
    @Operation(summary = "锁定/解锁指定用户", description = "切换锁定和解锁状态;<br/>锁定用户不能登陆;<br/>" + NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Void> lock(
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        final SystemUser user = systemUserService.getById(userId);
        final String message = user.getAccountNonLocked() ? "已锁定" : "已解锁";
        user.setAccountNonLocked(!user.getAccountNonLocked());
        systemUserService.updateById(user);
        return Res.of(null, message);
    }

    @GetMapping(Constant.Api.PAGE)
    @Operation(summary = "分页查询用户账号信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public ResPage<SystemUserVo> page(
            @ParameterObject SystemUserPageParam pageParam,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return systemUserService.pageByParam(pageParam, SystemUserVo::new);
    }

    @PostMapping("resetPassword")
    @Operation(summary = "重置用户的密码", description = NOT_CONFIG_ADMIN + "<br/>返回新密码")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<String> reset(@RequestBody @Validated ResetPasswordForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final Long userId = form.getUserId();
        final String newPass = form.getNewPass();
        rolePermissionService.forbiddenConfigAdminUser(userId);
        String pwd = ObjectUtils.isEmpty(newPass) ? UUID.randomUUID().toString() : newPass;
        systemUserService.changePwd(userId, pwd);
        return Res.of(pwd, "修改成功");
    }

    @PostMapping("roleAdd")
    @Operation(summary = "为指定用户添加角色", description = NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<RelationUserRole>> roleAdd(@RequestBody @Validated UserRoleForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        rolePermissionService.forbiddenConfigAdminUser(form.getUserId());
        systemRoleService.validateRoleId(form.getRoles().stream().map(RelationUserRoleForm::getRoleId).toList());
        final List<RelationUserRole> roleList = relationUserRoleService.add(form.getUserId(), form.getRoles());
        return Res.of(roleList);
    }

    @PostMapping("roleConfig")
    @Operation(summary = "为指定用户配置角色", description = "如果给出的角色尚未持有,添加持有;如果已持有,更新过期时间;删除未给出的角色持有<br/>" + NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<RelationUserRole>> roleConfig(@RequestBody @Validated UserRoleForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        final Long userId = form.getUserId();
        final List<RelationUserRoleForm> roles = form.getRoles();
        rolePermissionService.forbiddenConfigAdminUser(userId);
        systemRoleService.validateRoleId(roles.stream().map(RelationUserRoleForm::getRoleId).toList());
        final List<RelationUserRole> roleList = relationUserRoleService.config(userId, roles);
        return Res.of(roleList);
    }

    @PostMapping("roleDel")
    @Operation(summary = "为指定用户删除角色", description = NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Void> roleDel(@RequestBody @Validated UserRoleDelForm form, @SuppressWarnings("unused") HttpServletRequest request) {
        rolePermissionService.forbiddenConfigAdminUser(form.getUserId());
        relationUserRoleService.del(form.getUserId(), form.getRoleId());
        return Res.of(null);
    }

    @GetMapping("roleList")
    @Operation(summary = "查询用户持有的角色")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<List<SystemUserBo>> roleList(
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        return Res.of(rolePermissionService.listAuthorityByUserId(Collections.singleton(userId)));
    }

    @GetMapping("userInfoFind")
    @Operation(summary = "查询指定用户的个人信息")
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserInfoVo> userInfoFind(
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId,
            @SuppressWarnings("unused") HttpServletRequest request
    ) {
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "未找到用户个人信息,请先录入");
        }
        final SystemUserInfoVo vo = new SystemUserInfoVo(userInfo);
        return Res.of(vo);
    }

    @PostMapping("userInfoUpdate")
    @Operation(summary = "修改指定用户的个人信息", description = NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.Security.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Object> userInfoUpdate(
            @SuppressWarnings("unused") HttpServletRequest request,
            @RequestBody @Validated SystemUserInfoForm param,
            @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId
    ) {
        rolePermissionService.forbiddenConfigAdminUser(userId);
        systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(null, "修改成功");
    }

}
