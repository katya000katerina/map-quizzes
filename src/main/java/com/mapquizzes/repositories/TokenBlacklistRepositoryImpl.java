package com.mapquizzes.repositories;

import com.mapquizzes.config.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistRepositoryImpl implements TokenBlacklistRepository {
    @Override
    @CachePut(CacheConfig.ACCESS_TOKEN_BLACKLIST_CACHE_NAME)
    public String blacklistAccessToken(String token) {
        return token;
    }

    @Override
    @Cacheable(value = CacheConfig.ACCESS_TOKEN_BLACKLIST_CACHE_NAME, unless = "#result == null")
    public String getBlacklistedAccessToken(String token) {
        return null;
    }

    @Override
    @CachePut(CacheConfig.REFRESH_TOKEN_BLACKLIST_CACHE_NAME)
    public String blacklistRefreshToken(String token) {
        return token;
    }

    @Override
    @Cacheable(value = CacheConfig.REFRESH_TOKEN_BLACKLIST_CACHE_NAME, unless = "#result == null")
    public String getBlacklistedRefreshToken(String token) {
        return null;
    }
}
