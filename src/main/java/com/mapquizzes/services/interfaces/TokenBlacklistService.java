package com.mapquizzes.services.interfaces;

public interface TokenBlacklistService {
    String blacklistToken(String token);

    String getBlacklistedToken(String token);
}
