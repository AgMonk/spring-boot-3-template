package com.gin.springboot3template.sys.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码接口
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/7 09:50
 */
@Controller
@RequestMapping("/sys/verifyCode")
@RequiredArgsConstructor
@Api(tags = "验证码")
public class VerifyCodeController {
    public static final String VERIFY_CODE_KEY = "vc";
    private final Producer producer;

    @GetMapping("/image")
    @ApiOperation("图片格式")
    public void image(@ApiIgnore HttpServletResponse response,@ApiIgnore HttpSession httpSession) throws IOException {
        final BufferedImage image = createImage(httpSession);
        //响应图片
        response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE);
        ImageIO.write(image,"jpeg",response.getOutputStream());
    }

    @GetMapping("/base64")
    @ApiOperation("Base64格式")
    @ResponseBody
    public String base64(@ApiIgnore HttpSession httpSession) throws IOException {
        //生成验证码
        final BufferedImage image = createImage(httpSession);
        //响应图片
        final FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        ImageIO.write(image,"jpeg",os);
        //返回 base64
        return Base64.encodeBase64String(os.toByteArray());
    }

    private BufferedImage createImage(HttpSession httpSession) {
        //生成验证码
        final String verifyCode = producer.createText();
        //保存到 session 中（或redis中）
        httpSession.setAttribute(VERIFY_CODE_KEY,verifyCode);
        //生成图片
        return producer.createImage(verifyCode);
    }
}
