package com.gin.springboot3template.sys.security.vo;

import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.sys.entity.SystemUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

/**
 * 用户认证、权限信息
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 14:36
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "用户认证/授权信息")
public class MyUserDetails extends BaseVo {
    @Schema(description = "用户名", title = "username")
    private String username;
    @Schema(description = "账号未过期")
    private boolean accountNonExpired;
    @Schema(description = "账号未锁定")
    private boolean accountNonLocked;
    @Schema(description = "密码未过期")
    private boolean credentialsNonExpired;
    @Schema(description = "是否可用")
    private boolean enabled;
    @Schema(description = "权限")
    private Set<GrantedAuthority> authorities;

    /**
     * 获取当前用户认证/授权信息
     * @return 用户认证/授权信息
     */
    public static MyUserDetails of() {
        return of(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * 从 authentication 中获取用户认证/授权信息
     * @param authentication authentication
     * @return 用户认证/授权信息
     */
    public static MyUserDetails of(Authentication authentication) {
        final MyUserDetails details = new MyUserDetails();
        details.with(((UserDetails) authentication.getPrincipal()));
        return details;
    }

    public MyUserDetails with(UserDetails userDetails) {
        BeanUtils.copyProperties(userDetails, this);
        return this;
    }

    public MyUserDetails with(SystemUser systemUser) {
        BeanUtils.copyProperties(systemUser, this);
        return this;
    }
}
