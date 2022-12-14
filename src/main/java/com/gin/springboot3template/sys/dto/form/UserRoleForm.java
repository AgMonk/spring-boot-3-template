package com.gin.springboot3template.sys.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/19 16:02
 */
@Schema(description = "用户角色表单")
@Getter
@Setter
public class UserRoleForm extends UserIdForm {
    @Schema(description = "角色信息")
    @NotEmpty
    List<RelationUserRoleForm> roles;
}   
