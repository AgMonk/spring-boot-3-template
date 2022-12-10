package com.gin.springboot3template.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gin.springboot3template.sys.dao.SystemUserDao;
import com.gin.springboot3template.sys.entity.SystemUser;
import com.gin.springboot3template.sys.service.SystemUserService;
import org.springframework.stereotype.Service;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 16:47
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserDao, SystemUser> implements SystemUserService {

}   
