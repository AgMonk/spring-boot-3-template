package com.gin.springboot3template.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gin.springboot3template.sys.entity.SystemUserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:44
 */

@Transactional(rollbackFor = Exception.class)
public interface SystemUserInfoService extends MyService<SystemUserInfo> {
    /**
     * 根据用户名查询用户个人信息
     * @param userId 用户id
     * @return 个人信息
     */
    default SystemUserInfo getByUserId(long userId) {
        final QueryWrapper<SystemUserInfo> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return getOne(qw);
    }

    /**
     * 保存或更新用户个人信息
     * @param userId 用户id
     * @param param  参数
     */
    default SystemUserInfo saveOrUpdate(Long userId, SystemUserInfo.Param param) {
        final SystemUserInfo userInfo = getByUserId(userId);
        if (userInfo == null) {
            //不存在用户信息 添加
            final SystemUserInfo build = param.build(userId);
            save(build);
            return build;
        } else {
            // 已存在用户信息 修改
            BeanUtils.copyProperties(param, userInfo);
            updateById(userInfo);
            return userInfo;
        }
    }
}