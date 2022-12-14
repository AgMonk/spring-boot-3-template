package com.gin.springboot3template.sys.config.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

/**
 * RedisRedis-Mybatis缓存
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/4/18 10:40
 **/
@Data
@Slf4j
public class RedisMybatisCache implements Cache {

    private final String id;
    private RedisTemplate<String, Object> redisJsonTemplate;
    BoundHashOperations<String, String, Object> hash;

    public RedisMybatisCache(String id) {
        String key = "Mybatis:" + id.substring(id.lastIndexOf(".") + 1);
        this.id = key;
        log.info("[Redis]初始化: " + key);
    }

    private static String getKey(Object key) {
        return DigestUtils.md5DigestAsHex(key.toString().getBytes());
    }

    private BoundHashOperations<String, String, Object> getHash() {

        if (this.redisJsonTemplate==null){
            //noinspection unchecked
            this.redisJsonTemplate = (RedisTemplate<String, Object>) SpringContextUtils.getContext().getBean("redisJsonTemplate", new TypeReference<RedisTemplate<String, Object>>() {
            });
        }

        //noinspection ConstantConditions
        if (this.hash == null || !redisJsonTemplate.hasKey(id)) {
            this.hash = redisJsonTemplate.boundHashOps(id);
        }
        return this.hash;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (value == null) {
            return;
        }
        String k = getKey(key);
        log.debug("[Redis][{}]保存: {} -> {}", id, k, value);
        getHash().putIfAbsent(k, value);
    }

    @Override
    public Object getObject(Object key) {
        String k = getKey(key);
        Object o = getHash().get(k);
        if (o != null) {
            log.debug("[Redis][{}]命中: {}", id, k);
        } else {
            log.debug("[Redis][{}]未命中: {}", id, k);
        }
        return o;
    }

    @Override
    public Object removeObject(Object key) {
        String k = getKey(key);
        Object o = getObject(key);
        if (o != null) {
            log.debug("[Redis][{}]移除: {}", id, k);
            getHash().delete(k);
        }
        return o;
    }

    @Override
    public void clear() {
        log.info("[Redis]刷新: {}", id);
        redisJsonTemplate.delete(id);
    }

    @Override
    public int getSize() {
        //noinspection ConstantConditions
        return Math.toIntExact(getHash().size());
    }

}