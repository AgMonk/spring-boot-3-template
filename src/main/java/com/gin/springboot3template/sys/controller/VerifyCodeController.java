package com.gin.springboot3template.sys.controller;

import com.gin.springboot3template.sys.bo.Constant;
import com.gin.springboot3template.sys.response.Res;
import com.gin.springboot3template.sys.service.CaptchaService;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 验证码接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/7 09:50
 */
@Controller
@RequestMapping("/sys/verifyCode")
@RequiredArgsConstructor
@Tag(name = "验证码接口")
public class VerifyCodeController {
    private final CaptchaService captchaService;

    /**
     * 生成验证码 写入到Session中并写出到流
     * @param httpSession  session
     * @param outputStream 输出流
     */
    private static void createCaptcha(HttpSession httpSession, OutputStream outputStream) {
        Captcha captcha = new ArithmeticCaptcha(150, 50, 3);
        final String text = captcha.text();
        httpSession.setAttribute(Constant.Security.VERIFY_CODE_KEY, text);
        captcha.out(outputStream);
    }

    @GetMapping("/base64")
    @Operation(summary = "Base64格式", description = "获取验证码")
    @ResponseBody
    public Res<String> base64(@Parameter(hidden = true) HttpSession httpSession) throws IOException {
        return Res.of(captchaService.create(httpSession.getId(), ArithmeticCaptcha.class));
    }

    @GetMapping("/image")
    @Operation(summary = "图片格式", description = "获取验证码")
    public void image(@Parameter(hidden = true) HttpServletResponse response, @Parameter(hidden = true) HttpSession httpSession) throws IOException {
        //响应图片
        response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE);
        captchaService.create(httpSession.getId(), ArithmeticCaptcha.class, response.getOutputStream());
    }

}
