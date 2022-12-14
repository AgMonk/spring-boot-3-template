package com.gin.springboot3template.sys.security.service;

import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.security.bo.MyUserDetails;
import com.gin.springboot3template.sys.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/6 15:19
 */
@Service
@RequiredArgsConstructor
public class MyUserDetailsServiceImpl implements UserDetailsService, UserDetailsPasswordService {

    private final SystemUserService systemUserService;

    /**
     * 当前用户
     * @return 当前用户
     */
    public SystemUser currentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        return systemUserService.getByUsername(username);
    }

    /**
     * 根据用户名查询用户的认证授权信息
     * @param username 用户名
     * @return org.springframework.security.core.userdetails.UserDetails
     * @throws UsernameNotFoundException 异常
     * @since 2022/12/6 15:03
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final SystemUser systemUser = systemUserService.getByUsername(username);
        if (systemUser == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return MyUserDetails.of(systemUser);
    }

    /**
     * 修改密码
     * @param user        用户
     * @param newPassword 新密码
     * @return UserDetails
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        final SystemUser systemUser = systemUserService.getByUsername(user.getUsername());
        systemUser.setPassword(newPassword);
        systemUserService.updateById(systemUser);
        return MyUserDetails.of(systemUser);
    }
}