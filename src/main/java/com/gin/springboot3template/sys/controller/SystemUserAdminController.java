package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.security.service.MyUserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    private final MyUserDetailsServiceImpl myUserDetailsService;

    @PostMapping("addRole")
    @Operation(summary = "为指定用户添加角色")
    public void addRole() {
//todo
    }

    @PostMapping("configRole")
    @Operation(summary = "为指定用户配置角色")
    public void configRole() {
//todo
    }

    @PostMapping("delRole")
    @Operation(summary = "为指定用户删除角色")
    public void delRole() {
//todo
    }

    @GetMapping("listRole")
    @Operation(summary = "查询用户持有的角色")
    public void listRole() {
//todo
    }

    @PostMapping("resetPassword")
    @Operation(summary = "重置用户的密码")
    public void reset() {
//todo
    }

}
