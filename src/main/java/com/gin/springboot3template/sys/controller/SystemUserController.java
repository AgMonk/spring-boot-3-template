package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.annotation.MyRestController;
import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.bo.SystemUserBo;
import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.dto.form.LoginForm;
import com.gin.springboot3template.sys.dto.form.RegForm;
import com.gin.springboot3template.sys.dto.form.SystemUserInfoForm;
import com.gin.springboot3template.sys.entity.SystemUserAvatar;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import com.gin.springboot3template.sys.exception.BusinessException;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.security.utils.MySecurityUtils;
import com.gin.springboot3template.sys.security.vo.MyUserDetailsVo;
import com.gin.springboot3template.sys.service.RolePermissionService;
import com.gin.springboot3template.sys.service.SystemUserAvatarService;
import com.gin.springboot3template.sys.service.SystemUserInfoService;
import com.gin.springboot3template.sys.service.SystemUserService;
import com.gin.springboot3template.sys.validation.Password;
import com.gin.springboot3template.sys.vo.SystemUserInfoVo;
import com.gin.springboot3template.sys.vo.SystemUserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

import static com.gin.springboot3template.sys.bo.Constant.Security.PASSWORD_MAX_LENGTH;
import static com.gin.springboot3template.sys.bo.Constant.Security.PASSWORD_MIN_LENGTH;
import static org.springframework.http.MediaType.*;

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
    private final SystemProperties systemProperties;
    private final RolePermissionService rolePermissionService;
    private final SystemUserAvatarService systemUserAvatarService;

    private static Long getUserId() {
        return MySecurityUtils.currentUserDetails().getId();
    }

    @PostMapping("changePwd")
    @Operation(summary = "修改密码", description = "修改成功后会自动登出,需要重新登陆")
    public void changePwd(
            @SuppressWarnings("unused") HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam @Parameter(description = "旧密码") String oldPass,
            @RequestParam @Parameter(description = "新密码,长度范围为 [" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "]") @Password String newPass
    ) throws ServletException, IOException {
        final Long userId = getUserId();
        systemUserService.changePwd(userId, oldPass, newPass);
//        登出
        request.getRequestDispatcher(Constant.Security.LOGOUT_URI).forward(request, response);
    }

    @PostMapping("login")
    @Operation(summary = "登陆", description = "假登陆接口 ,用于生成 doc;<br/>需要先获取验证码,参数可以传body也可以传form")
    public Res<MyUserDetailsVo> login(@RequestBody @Validated @SuppressWarnings("unused") LoginForm loginForm) {
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
    public Res<SystemUserVo> reg(@RequestBody @Validated RegForm regForm) {
        if (!systemProperties.isNewUser()) {
            throw BusinessException.of(HttpStatus.FORBIDDEN, "注册功能已关闭");
        }
        return Res.of(new SystemUserVo(systemUserService.reg(regForm)), "注册成功");
    }

    @PostMapping("token")
    @Operation(summary = "查询自己的认证/授权信息", description = "包含用户名,ID,账号状态,权限信息;<br/>可以用来查询登陆状态,以及更新CSRF TOKEN")
    public Res<SystemUserBo> token() {
        return Res.of(rolePermissionService.listAuthorityByUserId(Collections.singleton(MyUserDetailsVo.of().getId())).get(0));
    }

    @PostMapping(value = "avatar/delete")
    @Operation(summary = "用户头像删除")
    public Res<SystemUserAvatar> userAvatarDelete() {
        return Res.of(systemUserAvatarService.deleteByUserId(getUserId()), "删除成功");
    }

    @PostMapping(value = "avatar/upload", consumes = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE, IMAGE_GIF_VALUE})
    @Operation(summary = "用户头像上传")
    public Res<SystemUserAvatar> userAvatarUpload(MultipartFile file) throws IOException {
        final Long userId = getUserId();
        systemUserAvatarService.deleteByUserId(userId);
        return Res.of(systemUserAvatarService.uploadWithUserId(file, userId), "上传成功");
    }

    @GetMapping("userInfoFind")
    @Operation(summary = "查询自己的个人信息")
    public Res<SystemUserInfoVo> userInfoFind() {
        final Long userId = getUserId();
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "未找到用户个人信息,请先录入");
        }
        final SystemUserInfoVo vo = new SystemUserInfoVo(userInfo);
        //todo 用户头像查询

        return Res.of(vo);
    }

    @PostMapping("userInfoUpdate")
    @Operation(summary = "修改自己的个人信息")
    public Res<Void> userInfoUpdate(@RequestBody @Validated SystemUserInfoForm param) {
        final Long userId = getUserId();
        systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(null, "修改成功");
    }


}
