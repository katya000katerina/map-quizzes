package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.PrincipalCertificateImageDto;
import com.mapquizzes.models.dto.PrincipalProfileImageDto;
import com.mapquizzes.models.entities.UserEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface UserImageService {
    PrincipalProfileImageDto getPrincipalProfileImageDto(Principal principal);
    PrincipalCertificateImageDto getPrincipalCertificateImageDto(Principal principal);

    PrincipalProfileImageDto saveOrUpdate(MultipartFile file, Principal principal);

    void deleteByUser(UserEntity entity);
}
