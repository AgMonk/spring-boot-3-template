package com.gin.springboot3template.sys.config.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

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
    BoundHashOperations<String, String, Object> hash;
    private RedisTemplate<String, Object> jsonTemplate;

    public RedisMybatisCache(String id) {
        String key = "Mybatis:" + id.substring(id.lastIndexOf(".") + 1);
        this.id = key;
        log.info("[Redis]初始化: " + key);
    }

    private static String getKey(Object key) {
        return DigestUtils.md5DigestAsHex(key.toString().getBytes());
    }

    @Override
    public void clear() {
        log.debug("[Redis]刷新: {}", id);
        jsonTemplate.delete(id);
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
    public int getSize() {
        //noinspection ConstantConditions
        return Math.toIntExact(getHash().size());
    }

    @Override
    public void putObject(Object key, Object value) {
        if (value == null) {
            return;
        }
        String k = getKey(key);
        log.debug("[Redis][{}]保存: {} -> {}", id, k, value);
        getHash().putIfAbsent(k, value);
        if (getSize() == 1) {
            jsonTemplate.expire(id, 5, TimeUnit.MINUTES);
        }
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

    private BoundHashOperations<String, String, Object> getHash() {

        if (this.jsonTemplate == null) {
            //noinspection unchecked
            this.jsonTemplate = (RedisTemplate<String, Object>) SpringContextUtils.getContext().getBean("jsonTemplate",
                                                                                                        new TypeReference<RedisTemplate<String, Object>>() {
                                                                                                        });
        }

        //noinspection ConstantConditions
        if (this.hash == null || !jsonTemplate.hasKey(id)) {
            this.hash = jsonTemplate.boundHashOps(id);
        }
        return this.hash;
    }

}
