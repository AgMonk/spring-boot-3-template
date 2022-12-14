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
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * ????????????
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/13 10:28
 */
@MyRestController(SystemUserController.API_PREFIX)
@RequiredArgsConstructor
@Tag(name = "????????????")
@Slf4j
public class SystemUserController {
    /**
     * ??????????????????
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
    @Operation(summary = "????????????", description = "??????????????????????????????,??????????????????")
    public void changePwd(
            @SuppressWarnings("unused") HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam @Parameter(description = "?????????") String oldPass,
            @RequestParam @Parameter(description = "?????????,??????????????? [" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "]") @Password String newPass
    ) throws ServletException, IOException {
        final Long userId = getUserId();
        systemUserService.changePwd(userId, oldPass, newPass);
//        ??????
        request.getRequestDispatcher(Constant.Security.LOGOUT_URI).forward(request, response);
    }

    @PostMapping("login")
    @Operation(summary = "??????", description = "??????????????? ,???????????? doc;<br/>????????????????????????,???????????????body????????????form")
    public Res<MyUserDetailsVo> login(@RequestBody @Validated @SuppressWarnings("unused") LoginForm loginForm) {
        System.out.println("login...");
        return null;
    }

    @PostMapping("logout")
    @Operation(summary = "??????", description = "??????????????? ,???????????? doc")
    public void logout() {
        System.out.println("logout...");
    }

    @PostMapping("reg")
    @Operation(summary = "????????????")
    public Res<SystemUserVo> reg(@RequestBody @Validated RegForm regForm) {
        if (!systemProperties.isNewUser()) {
            throw BusinessException.of(HttpStatus.FORBIDDEN, "?????????????????????");
        }
        return Res.of(new SystemUserVo(systemUserService.reg(regForm)), "????????????");
    }

    @PostMapping("token")
    @Operation(summary = "?????????????????????/????????????", description = "???????????????,ID,????????????,????????????;<br/>??????????????????????????????,????????????CSRF TOKEN")
    public Res<SystemUserBo> token() {
        return Res.of(rolePermissionService.listAuthorityByUserId(Collections.singleton(MyUserDetailsVo.of().getId())).get(0));
    }

    @PostMapping(value = "avatar/delete")
    @Operation(summary = "??????????????????")
    public Res<SystemUserAvatar> userAvatarDelete() {
        return Res.of(systemUserAvatarService.deleteByUserId(getUserId()), "????????????");
    }

    @PostMapping(value = "avatar/upload", consumes = {MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "??????????????????")
    public Res<SystemUserAvatar> userAvatarUpload(MultipartFile file) throws IOException {
        final Long userId = getUserId();

        systemUserAvatarService.validateMultipartFile(file);

        systemUserAvatarService.deleteByUserId(userId);
        return Res.of(systemUserAvatarService.uploadWithUserId(file, userId), "????????????");
    }

    @GetMapping("userInfoFind")
    @Operation(summary = "???????????????????????????")
    public Res<SystemUserInfoVo> userInfoFind() {
        final Long userId = getUserId();
        final SystemUserInfo userInfo = systemUserInfoService.getByUserId(userId);
        if (userInfo == null) {
            throw BusinessException.of(HttpStatus.NOT_FOUND, "???????????????????????????,????????????");
        }
        final SystemUserInfoVo vo = new SystemUserInfoVo(userInfo);

        //??????????????????
        final SystemUserAvatar avatar = systemUserAvatarService.getByUserId(userId);
        if (avatar != null) {
            vo.setAvatar(avatar.getFilePath());
        }

        return Res.of(vo);
    }

    @PostMapping("userInfoUpdate")
    @Operation(summary = "???????????????????????????")
    public Res<Void> userInfoUpdate(@RequestBody @Validated SystemUserInfoForm param) {
        final Long userId = getUserId();
        systemUserInfoService.saveOrUpdate(userId, param);
        return Res.of(null, "????????????");
    }


}
