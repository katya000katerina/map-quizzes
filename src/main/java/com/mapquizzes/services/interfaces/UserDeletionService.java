package com.mapquizzes.services.interfaces;

import java.security.Principal;

public interface UserDeletionService {
    void deletePrincipal(Principal principal, String accessToken, String refreshToken);
}
