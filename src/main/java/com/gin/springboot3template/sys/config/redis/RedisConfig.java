package com.gin.springboot3template.sys.config.redis;

import com.gin.springboot3template.sys.utils.SpringContextUtils;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


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
    private final RedisTemplate<Object, Object> redisTemplate;

    public static <T> RedisTemplate<String, T> createRedisJsonTemplate(String name) {
        log.info("创建 RedisTemplate:" + name);
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(SpringContextUtils.getContext().getBean(RedisConnectionFactory.class));
        //String的序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    public static RedisTemplate<String, Object> createRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(SpringContextUtils.getContext().getBean(RedisConnectionFactory.class));
        //String的序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
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

    /**
     * RedisTemplate 构造器
     * @param <K> Key类型
     * @param <V> Value类型
     */
    public static class RedisTemplateBuilder<K, V> {
        final RedisTemplate<K, V> template = new RedisTemplate<>();

        public RedisTemplateBuilder(RedisConnectionFactory redisConnectionFactory) {
            template.setConnectionFactory(redisConnectionFactory);
        }

        public RedisTemplate<K, V> build() {
            template.afterPropertiesSet();
            return template;
        }

        public RedisTemplateBuilder<K, V> setHashKeySerializer(RedisSerializer<?> hashKeySerializer) {
            template.setHashKeySerializer(hashKeySerializer);
            return this;
        }

        public RedisTemplateBuilder<K, V> setHashValueSerializer(RedisSerializer<?> hashValueSerializer) {
            template.setHashValueSerializer(hashValueSerializer);
            return this;
        }

        public RedisTemplateBuilder<K, V> setKeySerializer(RedisSerializer<?> serializer) {
            template.setKeySerializer(serializer);
            return this;
        }

        public RedisTemplateBuilder<K, V> setValueSerializer(RedisSerializer<?> serializer) {
            template.setValueSerializer(serializer);
            return this;
        }
    }

}
