package com.gin.springboot3template.sys.bo;

import com.gin.springboot3template.sys.base.BaseBo;
import com.gin.springboot3template.sys.entity.SystemPermission;
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
@Schema(name = "角色及其持有的权限")
public class RolePermission extends BaseBo {
    @Schema(description = "名称")
    String name;
    @Schema(description = "中文名称")
    String nameZh;
    @Schema(description = "备注")
    String remark;

    @Schema(description = "权限")
    List<SystemPermission> permissions;

}   
