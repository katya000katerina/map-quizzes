package com.mapquizzes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;

@Configuration
@PropertySource("classpath:application.yml")
public class CacheConfig {
    public final static String ACCESS_TOKEN_BLACKLIST_CACHE_NAME = "accessTokenBlacklist";
    public final static String REFRESH_TOKEN_BLACKLIST_CACHE_NAME = "refreshTokenBlacklist";
    public final static String USER_CACHE_NAME = "userCache";
    public final static String QUIZ_CACHE_NAME = "quizCache";
    @Value("${map-quizzes.security.jwt.access-token.expiration-time}")
    private long accessTokenExpTime;
    @Value("${map-quizzes.security.jwt.refresh-token.expiration-time}")
    private long refreshTokenExpTime;
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(redisHost, redisPort));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
                .withCacheConfiguration(ACCESS_TOKEN_BLACKLIST_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMillis(accessTokenExpTime)))
                .withCacheConfiguration(REFRESH_TOKEN_BLACKLIST_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMillis(refreshTokenExpTime)))
                .withCacheConfiguration(USER_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofDays(1)))
                .withCacheConfiguration(QUIZ_CACHE_NAME,
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofDays(1)));
    }
}
