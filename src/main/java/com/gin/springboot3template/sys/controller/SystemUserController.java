package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.security.service.MyUserDetailsServiceImpl;
import com.gin.springboot3template.sys.security.vo.MyUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 用户接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/13 10:28
 */
@MyRestController("/sys/user")
@RequiredArgsConstructor
@Tag(name = "用户接口")
public class SystemUserController {
    private final MyUserDetailsServiceImpl myUserDetailsService;

    @PostMapping("token")
    @Operation(summary = "获取用户认证/授权信息", description = "包含用户名,账号状态,权限信息")
    public Res<MyUserDetails> token() {
        System.out.println("1 = " + 1);
        return Res.of(MyUserDetails.of());
    }
}   
