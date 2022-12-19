package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.config.SystemConfig;
import com.gin.springboot3template.sys.dto.LoginForm;
import com.gin.springboot3template.sys.dto.RegForm;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.security.utils.MySecurityUtils;
import com.gin.springboot3template.sys.security.vo.MyUserDetailsVo;
import com.gin.springboot3template.sys.service.SystemUserInfoService;
import com.gin.springboot3template.sys.service.SystemUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

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
    private final SystemUserService systemUserService;
    private final SystemUserInfoService systemUserInfoService;
    private final SystemConfig systemConfig;

    @PostMapping("changePwd")
    @Operation(summary = "修改密码", description = "修改成功后会自动登出,需要重新登陆")
    public void changePwd(HttpServletRequest request
            , HttpServletResponse response
            , @RequestParam @Parameter(description = "旧密码") String oldPass
            , @RequestParam @Parameter(description = "新密码,长度范围为 [6,20]") @Length(min = 6, max = 20) String newPass
    ) throws ServletException, IOException {
        final Long userId = MySecurityUtils.currentUserDetails().getId();

        systemUserService.changePwd(userId, oldPass, newPass);
//        登出
        request.getRequestDispatcher(Constant.LOGOUT_URI).forward(request, response);
    }

    @GetMapping("findUserInfo")
    @Operation(summary = "查询自己的个人信息")
    public Res<SystemUserInfo.Vo> findUserInfo() {
        final Long userId = MySecurityUtils.currentUserDetails().getId();
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "未找到用户个人信息,请先录入");
        }
        final SystemUserInfo.Vo vo = new SystemUserInfo.Vo(userInfo);
        return Res.of(vo);
    }

    @PostMapping("login")
    @Operation(summary = "登陆", description = "假登陆接口 ,用于生成 doc;<br/>需要先获取验证码,参数可以传body也可以传form")
    public Res<MyUserDetailsVo> login(@RequestBody @Validated LoginForm loginForm) {
        System.out.println("login...");
        return null;
    }

    @PostMapping("logout")
    @Operation(summary = "登出", description = "假登出接口 ,用于生成 doc")
    public void logout() {
        System.out.println("logout...");
    }

    @PostMapping("reg")
    @Operation(summary = "注册用户")
    public Res<SystemUser.Vo> reg(@RequestBody @Validated RegForm regForm) {
        if (!systemConfig.isNewUser()) {
            throw BusinessException.of(HttpStatus.FORBIDDEN, "注册功能已关闭");
        }
        return Res.of(new SystemUser.Vo(systemUserService.reg(regForm)), "注册成功");
    }

    @PostMapping("token")
    @Operation(summary = "获取用户认证/授权信息", description = "包含用户名,ID,账号状态,权限信息;<br/>可以用来查询登陆状态,以及更新CSRF TOKEN")
    public Res<MyUserDetailsVo> token() {
        return Res.of(MyUserDetailsVo.of());
    }

    @PostMapping("updateUserInfo")
    @Operation(summary = "修改自己的个人信息")
    public Res<Void> updateUserInfo(@RequestBody @Validated SystemUserInfo.Param param) {
        final Long userId = MySecurityUtils.currentUserDetails().getId();
        systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(null, "修改成功");
    }

}
