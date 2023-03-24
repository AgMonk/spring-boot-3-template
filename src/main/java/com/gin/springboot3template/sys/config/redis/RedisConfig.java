package com.gin.springboot3template.sys.config.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


/**
 * Redis配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/4/16 15:46
 **/
@Configuration
@AllArgsConstructor
@EnableCaching
@Slf4j
public class RedisConfig {
    public static final String REDIS_CACHE_MANAGER = "redisCacheManager";

    @Bean
    @Primary
    public RedisTemplate<String, Object> jsonTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateBuilder<String, Object>(redisConnectionFactory)
                .setValueSerializer(new GenericJackson2JsonRedisSerializer())
                .setHashValueSerializer(new GenericJackson2JsonRedisSerializer())
                .build();
    }

    @Bean(name = RedisConfig.REDIS_CACHE_MANAGER)
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        final RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .computePrefixWith(cacheName -> "Cache:" + cacheName + ":")
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    @Bean
    public RedisTemplate<String, String> stringTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTemplateBuilder<String, String>(redisConnectionFactory)
                .setValueSerializer(new StringRedisSerializer())
                .setHashValueSerializer(new StringRedisSerializer())
                .build();
    }

    /**
     * RedisTemplate 构造器
     * key的序列化方式默认为string
     * @param <K> Key类型
     * @param <V> Value类型
     */
    public static class RedisTemplateBuilder<K, V> {
        private final RedisTemplate<K, V> template = new RedisTemplate<>();

        public RedisTemplateBuilder(RedisConnectionFactory redisConnectionFactory) {
            template.setConnectionFactory(redisConnectionFactory);
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setKeySerializer(new StringRedisSerializer());
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
