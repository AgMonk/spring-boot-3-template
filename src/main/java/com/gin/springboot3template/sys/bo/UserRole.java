package com.gin.springboot3template.sys.bo;

import com.gin.springboot3template.sys.base.BaseBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 用户持有的角色
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 17:57
 */
@Getter
@Setter
@NoArgsConstructor
public class UserRole extends BaseBo {

    @Schema(description = "角色和权限")
    List<RolePermission> rolePermissions;

}   
