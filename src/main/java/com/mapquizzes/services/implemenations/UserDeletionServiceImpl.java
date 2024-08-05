package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.services.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserDeletionServiceImpl implements UserDeletionService {
    private final UserService userService;
    private final FastestTimeService fastestTimeService;
    private final MistakeService mistakeService;
    private final UserImageService imageService;
    private final TokenBlacklistService blacklistService;

    @Transactional
    @Override
    public void deletePrincipal(Principal principal, String accessToken, String refreshToken) {
        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        blacklistService.blacklistAccessAndRefreshTokens(accessToken, refreshToken);

        fastestTimeService.deleteAllByUser(userEntity);
        mistakeService.deleteAllByUser(userEntity);
        imageService.deleteByUser(userEntity);
        userService.delete(userEntity);
    }
}
