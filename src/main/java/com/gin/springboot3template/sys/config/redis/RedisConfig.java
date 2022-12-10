package com.gin.springboot3template.sys.config.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;


/**
 * Redis配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/4/16 15:46
 **/
@Configuration
@AllArgsConstructor
@Slf4j
public class RedisConfig {
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisTemplate<Object, Object> redisTemplate;

    public <T> RedisTemplate<String, T> createRedisJsonTemplate(String name) {
        log.info("创建 RedisTemplate:" + name);
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        //String的序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, Object> redisJsonTemplate(RedisConnectionFactory redisConnectionFactory) {
        return createRedisJsonTemplate("json");
    }

    @PostConstruct
    void init() {
        log.info("{}", redisTemplate);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
    }

}
