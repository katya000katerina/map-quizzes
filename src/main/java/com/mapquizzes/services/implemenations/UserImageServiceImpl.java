package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.dto.PrincipalImageDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.UserImageEntity;
import com.mapquizzes.repositories.UserImageRepository;
import com.mapquizzes.services.interfaces.UserImageService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {
    private final UserImageRepository imageRepo;
    private final UserService userService;

    @Override
    public PrincipalImageDto saveOrUpdate(MultipartFile file, Principal principal) {
        UserImageEntity image = new UserImageEntity();

        UserEntity user = userService.getEntityByPrincipal(principal);
        image.setUser(user);

        try {
            image.setBytes(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileName = file.getOriginalFilename();
        image.setFileName(fileName);
        imageRepo.getIdByUser(user).ifPresent(image::setId);

        byte[] bytes = imageRepo.save(image).getBytes();

        return getPrincipalImageDto(fileName, bytes);
    }

    @Override
    public PrincipalImageDto getPrincipalImage(Principal principal) {
        UserEntity user = userService.getEntityByPrincipal(principal);
        Optional<UserImageEntity> optional = imageRepo.findByUser(user);

        if (optional.isPresent()) {
            UserImageEntity image = optional.get();
            return getPrincipalImageDto(image.getFileName(), image.getBytes());
        } else {
            try {
                File image = ResourceUtils.getFile("classpath:static/images/no image.png");// image from https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Simpleicons_Interface_user-black-close-up-shape.svg/1024px-Simpleicons_Interface_user-black-close-up-shape.svg.png
                byte[] bytes = Files.readAllBytes(image.toPath());
                return getPrincipalImageDto(image.getName(), bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private PrincipalImageDto getPrincipalImageDto(String fileName, byte[] bytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(getContentType(fileName)));
        headers.setContentLength(bytes.length);
        return new PrincipalImageDto(bytes, headers);
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        }
        return "application/octet-stream";
    }
}
