package com.gin.springboot3template.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.user.dao.SystemRoleDao;
import com.gin.springboot3template.user.entity.SystemRole;
import com.gin.springboot3template.user.service.SystemRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/14 16:58
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemRoleServiceImpl extends ServiceImpl<SystemRoleDao, SystemRole> implements SystemRoleService {
}