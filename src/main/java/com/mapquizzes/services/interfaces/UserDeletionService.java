package com.mapquizzes.services.interfaces;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;

public interface UserDeletionService {
    void deletePrincipal(Principal principal, HttpServletRequest request);
}
