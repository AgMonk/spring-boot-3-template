package com.gin.springboot3template.sys.security.bo;

import com.gin.springboot3template.sys.base.BaseBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 角色持有的权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 17:57
 */
@Getter
@Setter
@NoArgsConstructor
public class RolePermission extends BaseBo {
    @Schema(description = "角色ID")
    Long roleId;

    @Schema(description = "权限")
    List<String> permissions;

}   
