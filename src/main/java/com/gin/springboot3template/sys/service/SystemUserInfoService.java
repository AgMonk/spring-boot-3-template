package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:44
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemUserInfoService extends MyService<SystemUserInfo> {

    default SystemUserInfo getByUserId(long userId) {
        final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return getOne(qw);
    }

}