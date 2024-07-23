package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.PrincipalImageDto;
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
public class UserImageController {
    private final UserImageService service;

    @GetMapping("/current")
    public ResponseEntity<byte[]> getPrincipalImage(Principal principal) {
        PrincipalImageDto dto = service.getPrincipalImage(principal);
        return new ResponseEntity<>(dto.bytes(), dto.headers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<byte[]> uploadPrincipalImage(@RequestParam("image") MultipartFile file, Principal principal) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        PrincipalImageDto dto = service.saveOrUpdate(file, principal);
        return new ResponseEntity<>(dto.bytes(), dto.headers(), HttpStatus.OK);
    }
}
