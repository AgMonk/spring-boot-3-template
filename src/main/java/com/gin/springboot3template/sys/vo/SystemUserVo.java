package com.gin.springboot3template.sys.vo;

import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.entity.SystemUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "用户账号信息")
public class SystemUserVo extends BaseVo {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "账号未过期")
    private boolean accountNonExpired;
    @Schema(description = "账号未锁定")
    private boolean accountNonLocked;
    @Schema(description = "密码未过期")
    private boolean credentialsNonExpired;
    @Schema(description = "是否可用")
    private boolean enabled;

    public SystemUserVo(SystemUser systemUser) {
        BeanUtils.copyProperties(systemUser, this);
    }
}