package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.dto.TokensDto;
import com.mapquizzes.repositories.TokenBlacklistRepository;
import com.mapquizzes.services.interfaces.TokenBlacklistService;
import com.mapquizzes.utils.CookieTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final TokenBlacklistRepository blacklistRepository;

    @Override
    public void blacklistAccessAndRefreshTokens(String accessToken, String refreshToken) {
        blacklistTokens(accessToken, refreshToken);
    }

    @Override
    public void blacklistAccessAndRefreshTokens(HttpServletRequest request) {
        TokensDto tokens = CookieTokenUtils.extractAccessAndRefreshTokens(request);

        String accessToken = tokens.accessToken();
        String refreshToken = tokens.refreshToken();

        blacklistTokens(accessToken, refreshToken);
    }

    private void blacklistTokens(String accessToken, String refreshToken) {
        blacklistRepository.blacklistAccessToken(accessToken);
        blacklistRepository.blacklistRefreshToken(refreshToken);
    }
}
