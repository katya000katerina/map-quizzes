package com.mapquizzes.repositories;

public interface TokenBlacklistRepository {

    String blacklistAccessToken(String token);

    String getBlacklistedAccessToken(String token);

    String blacklistRefreshToken(String token);

    String getBlacklistedRefreshToken(String token);
}
