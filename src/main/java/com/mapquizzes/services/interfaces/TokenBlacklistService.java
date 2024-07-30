package com.mapquizzes.services.interfaces;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenBlacklistService {
    void blacklistAccessAndRefreshTokens(HttpServletRequest request);
}
