package com.mapquizzes.services.interfaces;

import com.mapquizzes.models.dto.PrincipalImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface UserImageService {
    PrincipalImageDto getPrincipalImage(Principal principal);

    PrincipalImageDto saveOrUpdate(MultipartFile file, Principal principal);
}
