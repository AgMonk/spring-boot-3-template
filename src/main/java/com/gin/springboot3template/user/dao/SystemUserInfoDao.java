package com.gin.springboot3template.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gin.springboot3template.sys.config.redis.RedisMybatisCache;
import com.gin.springboot3template.user.entity.SystemUserInfo;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:44
 */
@Mapper
@CacheNamespace(flushInterval = 5L * 60 * 1000, implementation = RedisMybatisCache.class)
public interface SystemUserInfoDao extends BaseMapper<SystemUserInfo> {
}
