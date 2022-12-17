package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.security.service.MyUserDetailsServiceImpl;
import com.gin.springboot3template.sys.security.vo.MyUserDetailsVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/13 10:28
 */
@MyRestController(SystemUserController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = "用户接口")
@Slf4j
public class SystemUserController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/sys/user";
    private final MyUserDetailsServiceImpl myUserDetailsService;

    @PostMapping("changePwd")
    @Operation(summary = "修改密码")
    public void changePwd() {
        //todo
    }

    @GetMapping("findUserInfo")
    @Operation(summary = "查询自己的个人信息")
    public void findUserInfo() {
        //todo
    }

    @PostMapping("login")
    @Operation(summary = "登陆", description = "需要先获取验证码,参数可以传body也可以传form")
    public void login(@RequestBody LoginForm loginForm) {
        //假登陆接口 ,用于生成 doc

        System.out.println("login...");
    }

    @GetMapping("logout")
    @Operation(summary = "登出")
    public void logout() {
        //假登出接口 ,用于生成 doc
        System.out.println("logout...");
    }

    @PostMapping("reg")
    @Operation(summary = "注册用户")
    public void reg() {
        //todo
    }

    @PostMapping("token")
    @Operation(summary = "获取用户认证/授权信息", description = "包含用户名,ID,账号状态,权限信息;<br/>可以用来查询登陆状态,以及更新CSRF TOKEN")
    public Res<MyUserDetailsVo> token() {
        return Res.of(MyUserDetailsVo.of());
    }

    @PostMapping("updateUserInfo")
    @Operation(summary = "修改自己的个人信息")
    public void updateUserInfo() {
        //todo
    }

    @Schema(description = "登录表单")
    @Getter
    @Setter
    public static class LoginForm {
        @Schema(description = "用户名")
        String username;
        @Schema(description = "密码")
        String password;
        @Schema(description = "验证码")
        String vc;
        @Schema(description = "记住我( true / yes / on / 1 表示开启) ")
        String rememberMe;
    }
}
