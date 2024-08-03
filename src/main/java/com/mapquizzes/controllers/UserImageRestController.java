package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.PrincipalProfileImageDto;
import com.mapquizzes.services.interfaces.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class UserImageRestController {
    private final UserImageService service;

    @GetMapping("/current")
    public ResponseEntity<byte[]> getPrincipalImage(Principal principal) {
        PrincipalProfileImageDto dto = service.getPrincipalProfileImageDto(principal);
        return new ResponseEntity<>(dto.bytes(), dto.headers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<byte[]> uploadPrincipalImage(@RequestParam("image") MultipartFile file, Principal principal) {
        PrincipalProfileImageDto dto = service.saveOrUpdate(file, principal);
        return new ResponseEntity<>(dto.bytes(), dto.headers(), HttpStatus.ACCEPTED);
    }
}
