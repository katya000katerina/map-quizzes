package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.AuthenticationDto;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.services.interfaces.UserDeletionService;
import com.mapquizzes.services.interfaces.UserService;
import com.mapquizzes.validation.groups.user.ChangePassword;
import com.mapquizzes.validation.groups.user.ChangeUsername;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getSignedInUser(Principal principal) {
        UserDto userDto = userService.getDtoByPrincipal(principal);
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping(value = "/username", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> changeUsername(@Validated(ChangeUsername.class) @RequestBody UserDto userDto,
                                                  Principal principal,
                                                  @CookieValue("accessToken") String accessToken,
                                                  @CookieValue("refreshToken") String refreshToken) {
        AuthenticationDto authDto = userService.changeUsername(userDto, principal, accessToken, refreshToken);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .headers(authDto.getTokenCookiesHeaders())
                .body(authDto.getUserDto());
    }

    @PatchMapping(value = "/password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> changePassword(@Validated(ChangePassword.class) @RequestBody UserDto user,
                                                  Principal principal) {
        UserDto userDto = userService.changePassword(user, principal);
        return ResponseEntity
                .accepted()
                .body(userDto);
    }

    @DeleteMapping("/current")
    public ResponseEntity<Void> deleteSignedInUser(Principal principal,
                                                   @CookieValue("accessToken") String accessToken,
                                                   @CookieValue("refreshToken") String refreshToken) {
        userDeletionService.deletePrincipal(principal, accessToken, refreshToken);
        return ResponseEntity.noContent().build();
    }
}
