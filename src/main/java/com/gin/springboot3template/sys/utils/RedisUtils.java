package com.gin.springboot3template.sys.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Redis工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/19 12:01
 */
public class RedisUtils {
    /**
     * 从Redis中查询指定key前缀的对象列表
     * @param template redisTemplate
     * @param prefix   key前缀
     * @param <T>      类型
     * @return 对象列表
     */
    public static <T> List<T> listByPrefix(RedisTemplate<String, T> template, String prefix) {
        final Set<String> keys = template.keys(prefix + "*");
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>();
        }
        final List<T> tagMessages = template.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(tagMessages)) {
            return new ArrayList<>();
        }
        return tagMessages;
    }
}   
