package com.gin.springboot3template.sys.security.bo;

import com.gin.springboot3template.sys.entity.SystemUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
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
public class MyUserDetails implements UserDetails {
    private String password;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Set<GrantedAuthority> authorities;

    public MyUserDetails(SystemUser systemUser) {
        BeanUtils.copyProperties(systemUser, this);


//直接给用户添加 admin权限
//        this.authorities = new HashSet<>();
//        this.authorities.add(new SimpleGrantedAuthority("ROLE_admin"));
    }
}
