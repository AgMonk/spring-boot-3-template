package com.gin.springboot3template.sys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 注册表单
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 16:48
 */
@Schema(description = "登录表单")
@Getter
@Setter
public class RegForm {
    @Schema(description = "用户名")
    @NotNull
    String username;
    @Schema(description = "密码,长度范围为 [6,20]")
    @NotNull
    @Length(min = 6, max = 20)
    String password;

    //todo 个人信息部分
}
