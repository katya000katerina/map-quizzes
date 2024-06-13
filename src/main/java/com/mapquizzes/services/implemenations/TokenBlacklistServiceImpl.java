package com.mapquizzes.services.implemenations;

import com.mapquizzes.config.CacheConfig;
import com.mapquizzes.services.interfaces.TokenBlacklistService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {

    @Override
    @CachePut(CacheConfig.TOKEN_BLACKLIST_CACHE_NAME)
    public String blacklistToken(String token) {
        return token;
    }

    @Override
    @Cacheable(value = CacheConfig.TOKEN_BLACKLIST_CACHE_NAME, unless = "#result == null")
    public String getBlacklistedToken(String token) {
        return null;
    }
}
