package com.gin.springboot3template.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.sys.dao.SystemUserDao;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.service.SystemUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:47
 */
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserDao, SystemUser> implements SystemUserService {

}   
