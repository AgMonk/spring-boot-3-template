package com.gin.springboot3template.sys.service.impl;

import com.gin.springboot3template.sys.config.SystemProperties;
import com.gin.springboot3template.sys.dao.SystemUserAvatarDao;
import com.gin.springboot3template.sys.entity.SystemUserAvatar;
import com.gin.springboot3template.sys.service.SystemUserAvatarService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/26 16:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemUserAvatarServiceImpl extends AttachmentServiceImpl<SystemUserAvatarDao, SystemUserAvatar> implements SystemUserAvatarService {
    public SystemUserAvatarServiceImpl(SystemProperties systemProperties) {
        super(systemProperties);
    }

    @Override
    public String attachPath() {
        return "/user/avatar";
    }
}