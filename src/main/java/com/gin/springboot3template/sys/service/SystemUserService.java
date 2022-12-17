package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.SystemUser;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:45
 */
public interface SystemUserService extends MyService<SystemUser> {
    /**
     * 修改密码
     * @param oldPass 旧密码
     * @param newPass 新密码
     */
    default void changePwd(String oldPass, String newPass) {
    }

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户信息
     */
    default SystemUser getByUsername(String username) {
        final QueryWrapper<SystemUser> qw = new QueryWrapper<>();
        qw.eq("username", username);
        return getOne(qw);
    }
}
