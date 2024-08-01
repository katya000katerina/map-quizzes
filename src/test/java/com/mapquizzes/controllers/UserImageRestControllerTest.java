package com.mapquizzes.controllers;

import com.mapquizzes.models.dto.PrincipalProfileImageDto;
import com.mapquizzes.services.interfaces.UserImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class UserImageRestControllerTest extends BaseControllerTest {
    @MockBean
    private UserImageService imageService;

    private static final String MOCK_IMAGE_CONTENT = "mock image bytes";
    private static final String MOCK_IMAGE_FILENAME = "mock-image.jpg";

    private byte[] mockImageBytes;
    private PrincipalProfileImageDto dto;
    private MockMultipartFile mockImage;

    @BeforeEach
    void setup() {
        mockImageBytes = MOCK_IMAGE_CONTENT.getBytes();
        dto = TestDataFactory.createPrincipalProfileImageDto(mockImageBytes);
        mockImage = new MockMultipartFile("image", MOCK_IMAGE_FILENAME,
                MediaType.IMAGE_JPEG_VALUE, mockImageBytes);
    }

    @Test
    @WithMockUser
    void testGetPrincipalImage_ReturnsOkAndImage() throws Exception {
        when(imageService.getPrincipalProfileImageDto(any(Principal.class)))
                .thenReturn(dto);

        mockMvc.perform(get("/api/v1/images/current"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(dto.headers().getContentType()))
                .andExpect(header().longValue("Content-Length", mockImageBytes.length))
                .andExpect(content().bytes(mockImageBytes));
    }

    @Test
    void testGetPrincipalImage_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/images/current"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testUploadPrincipalImage_ReturnsAcceptedAndImage() throws Exception {
        when(imageService.saveOrUpdate(eq(mockImage), any(Principal.class)))
                .thenReturn(dto);

        mockMvc.perform(multipart("/api/v1/images")
                        .file(mockImage))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(dto.headers().getContentType()))
                .andExpect(header().longValue("Content-Length", mockImageBytes.length))
                .andExpect(content().bytes(mockImageBytes));
    }

    @Test
    void testUploadPrincipalImage_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(multipart("/api/v1/images")
                        .file(mockImage))
                .andExpect(status().isUnauthorized());
    }

    private static class TestDataFactory {
        static PrincipalProfileImageDto createPrincipalProfileImageDto(byte[] bytes) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(bytes.length);
            return new PrincipalProfileImageDto(bytes, headers);
        }
    }
}