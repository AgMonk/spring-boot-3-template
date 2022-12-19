package com.gin.springboot3template.sys.vo;

import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author bx002
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "用户个人信息")
public class SystemUserInfoVo extends BaseVo {
    @Schema(description = "用户id")
    Long userId;
    @Schema(description = "昵称")
    String nickname;
    @Schema(description = "联系电话")
    String phone;
    @Schema(description = "生日(UNIX秒)")
    Long birthday;

    public SystemUserInfoVo(SystemUserInfo systemUserInfo) {
        BeanUtils.copyProperties(systemUserInfo, this);
    }
}