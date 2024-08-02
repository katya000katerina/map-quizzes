package com.mapquizzes.controllers;

import com.mapquizzes.services.interfaces.CertificateService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CertificateRestControllerTest extends BaseControllerTest {
    @MockBean
    private CertificateService certificateService;

    @Test
    @WithMockUser
    void downloadCertificate_ReturnsOkAndResource() throws Exception {
        byte[] mockByteArray = "mock byte array".getBytes();
        when(certificateService.getCertificate(any(Principal.class)))
                .thenReturn(mockByteArray);

        mockMvc.perform(get("/api/v1/certificate"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=certificate.pdf"))
                .andExpect(content().bytes(mockByteArray));
    }

    @Test
    void downloadCertificate_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(patch("/api/v1/certificate"))
                .andExpect(status().isUnauthorized());
    }
}