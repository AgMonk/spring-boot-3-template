package com.gin.springboot3template.sys.security.service;

import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

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
     * 返回是否有权限访问
     * @return 是否有权限访问
     */
    public boolean authorize(Authentication authentication, HttpServletRequest request, Integer id) {
        System.out.println("id = " + id);
        return false;
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
        return systemUser.createUser()
                .authorities(new ArrayList<>())
                .build();
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
        return systemUser.createUser()
                .authorities(new ArrayList<>())
                .build();
    }
}
