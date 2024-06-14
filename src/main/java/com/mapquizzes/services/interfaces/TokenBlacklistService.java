package com.mapquizzes.services.interfaces;

import com.mapquizzes.config.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public interface TokenBlacklistService {
    @CachePut(CacheConfig.ACCESS_TOKEN_BLACKLIST_CACHE_NAME)
    String blacklistAccessToken(String token);

    @Cacheable(value = CacheConfig.ACCESS_TOKEN_BLACKLIST_CACHE_NAME, unless = "#result == null")
    String getBlacklistedAccessToken(String token);

    @CachePut(CacheConfig.REFRESH_TOKEN_BLACKLIST_CACHE_NAME)
    String blacklistRefreshToken(String token);

    @Cacheable(value = CacheConfig.REFRESH_TOKEN_BLACKLIST_CACHE_NAME, unless = "#result == null")
    String getBlacklistedRefreshToken(String token);
}
