package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.UserDeletionService;
import com.mapquizzes.services.interfaces.UserService;
import com.mapquizzes.validation.groups.user.ChangePassword;
import com.mapquizzes.validation.groups.user.ChangeUsername;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService userService;
    private final UserDeletionService userDeletionService;

    @GetMapping("/current")
    public ResponseEntity<UserDto> getSignedInUser(Principal principal) {
        return ResponseEntity.ok(userService.getDtoByPrincipal(principal));
    }

    @PostMapping("/username")
    public ResponseEntity<AuthenticationDto> changeUsername(@Validated(ChangeUsername.class) @RequestBody UserDto user,
                                                            Principal principal,
                                                            HttpServletRequest request) {
        AuthenticationDto authDto = userService.changeUsername(user, principal, request);
        return ResponseEntity
                .ok()
                .headers(authDto.getTokenCookiesHeaders())
                .build();
    }

    @PostMapping("/password")
    public ResponseEntity<UserDto> changePassword(@Validated(ChangePassword.class) @RequestBody UserDto user,
                                                  Principal principal) {
        return ResponseEntity.ok(userService.changePassword(user, principal));
    }

    @DeleteMapping("/current")
    public ResponseEntity<Void> deleteSignedInUser(Principal principal, HttpServletRequest request) {
        userDeletionService.deletePrincipal(principal, request);
        return ResponseEntity.noContent().build();
    }
}
