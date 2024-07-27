package com.mapquizzes.controllers;

import com.mapquizzes.services.interfaces.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/certificate")
@RequiredArgsConstructor
public class CertificateRestController {
    private final CertificateService certificateService;

    @GetMapping
    public ResponseEntity<Resource> downloadCertificate(Principal principal) throws IOException {
        ByteArrayResource resource = new ByteArrayResource(certificateService.getCertificate(principal));
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate.pdf");
        return new ResponseEntity<>(resource, header, HttpStatus.OK);
    }
}
