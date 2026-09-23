package com.tiki.inventory.config;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisCacheConfig implements CachingConfigurer {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheConfig.class);
    private final RedisConnectionFactory connectionFactory;
    private final Duration ttl;

    public RedisCacheConfig(RedisConnectionFactory connectionFactory,
            @Value("${inventory.cache-ttl:60s}") Duration ttl) {
        this.connectionFactory = connectionFactory;
        this.ttl = ttl;
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(configuration).build();
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.warn("Redis GET lỗi cho key={}; fallback xuống database: {}", key, e.getMessage());
            }
            @Override public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.warn("Redis PUT lỗi cho key={}; request vẫn dùng dữ liệu DB: {}", key, e.getMessage());
            }
            @Override public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis EVICT lỗi cho key={}; TTL sẽ loại dữ liệu cũ: {}", key, e.getMessage());
            }
            @Override public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("Redis CLEAR lỗi; cache sẽ tự hết hạn theo TTL: {}", e.getMessage());
            }
        };
    }
}
