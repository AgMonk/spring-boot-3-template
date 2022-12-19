package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.dto.RegForm;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.service.RolePermissionService;
import com.gin.springboot3template.sys.service.SystemUserInfoService;
import com.gin.springboot3template.sys.service.SystemUserService;
import com.gin.springboot3template.sys.service.impl.SystemUserServiceImpl;
import com.gin.springboot3template.sys.validation.EntityId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.gin.springboot3template.sys.bo.Constant.*;

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

    @PostMapping("addRole")
    @Operation(summary = "为指定用户添加角色", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void addRole(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        validatedNotAdmin(userId);

//todo
    }

    @PostMapping("configRole")
    @Operation(summary = "为指定用户配置角色", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void configRole(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        validatedNotAdmin(userId);

//todo
    }

    @PostMapping("createUser")
    @Operation(summary = "创建用户")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUser.Vo> createUser(@RequestBody RegForm regForm) {
        return Res.of(new SystemUser.Vo(systemUserService.reg(regForm)), "创建成功");
    }

    @PostMapping("delRole")
    @Operation(summary = "为指定用户删除角色", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void delRole(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
//todo
        validatedNotAdmin(userId);


    }

    @GetMapping("findUserInfo")
    @Operation(summary = "查询指定用户的个人信息")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<SystemUserInfo.Vo> findUserInfo(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "未找到用户个人信息,请先录入");
        }
        final SystemUserInfo.Vo vo = new SystemUserInfo.Vo(userInfo);
        return Res.of(vo);
    }

    @GetMapping("listRole")
    @Operation(summary = "查询用户持有的角色")
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void listRole(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
//todo
    }

    @PostMapping("lock")
    @Operation(summary = "锁定/解锁指定用户", description = "锁定用户不能登陆<br/>" + MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public void lock(@RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        validatedNotAdmin(userId);

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
        validatedNotAdmin(userId);

        //todo
    }

    @PostMapping("updateUserInfo")
    @Operation(summary = "修改指定用户的个人信息", description = MESSAGE_NOT_CONFIG_ADMIN)
    @PreAuthorize(Constant.PRE_AUTHORITY_URI_OR_ADMIN)
    public Res<Object> updateUserInfo(@RequestBody @Validated SystemUserInfo.Param param, @RequestParam @EntityId(service = SystemUserServiceImpl.class) Long userId, HttpServletRequest request) {
        validatedNotAdmin(userId);


        systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(null, "修改成功");
    }

    /**
     * 不能对 持有 admin 角色 的用户操作
     * @param userId 用户id
     */
    private void validatedNotAdmin(long userId) {
        final List<String> authorities = rolePermissionService.getAuthorities(userId).stream().map(GrantedAuthority::getAuthority).toList();
        String roleAdmin = DEFAULT_ROLE_PREFIX + ROLE_ADMIN;
        if (authorities.contains(roleAdmin)) {
            throw BusinessException.of(HttpStatus.FORBIDDEN, MESSAGE_NOT_CONFIG_ADMIN);
        }
    }
}
