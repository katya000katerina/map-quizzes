package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.dto.PrincipalCertificateImageDto;
import com.mapquizzes.models.dto.PrincipalProfileImageDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.entities.UserImageEntity;
import com.mapquizzes.repositories.UserImageRepository;
import com.mapquizzes.services.interfaces.UserImageService;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {
    private final UserImageRepository imageRepo;
    private final UserService userService;
    @Value("${map-quizzes.resources.no-image}")
    private String noImagePath; // image from https://upload.wikimedia.org/wikipedia/commons/thumb/4/42/Simpleicons_Interface_user-black-close-up-shape.svg/1024px-Simpleicons_Interface_user-black-close-up-shape.svg.png

    @Override
    public PrincipalProfileImageDto saveOrUpdate(MultipartFile file, Principal principal) {
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

        return getPrincipalProfileImageDto(fileName, bytes);
    }

    @Override
    public PrincipalProfileImageDto getPrincipalProfileImageDto(Principal principal) {
        BiFunction<String, byte[], PrincipalProfileImageDto> func = this::getPrincipalProfileImageDto;
        return getPrincipalImage(principal, func);
    }

    @Override
    public PrincipalCertificateImageDto getPrincipalCertificateImageDto(Principal principal) {
        BiFunction<String, byte[], PrincipalCertificateImageDto> func = (fileName, bytes) -> {
            int lastDotIndex = fileName.lastIndexOf(".");
            String extension = lastDotIndex > 0
                    ? fileName.substring(lastDotIndex + 1)
                    : "png";
            return new PrincipalCertificateImageDto(bytes, extension);
        };
        return getPrincipalImage(principal, func);
    }

    private <R> R getPrincipalImage(Principal principal, BiFunction<String, byte[], R> func) {
        UserEntity user = userService.getEntityByPrincipal(principal);
        Optional<UserImageEntity> optional = imageRepo.findByUser(user);

        if (optional.isPresent()) {
            UserImageEntity image = optional.get();
            return func.apply(image.getFileName(), image.getBytes());
        } else {
            try {
                File image = ResourceUtils.getFile(noImagePath);
                byte[] bytes = Files.readAllBytes(image.toPath());
                return func.apply(image.getName(), bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private PrincipalProfileImageDto getPrincipalProfileImageDto(String fileName, byte[] bytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(getContentType(fileName)));
        headers.setContentLength(bytes.length);
        return new PrincipalProfileImageDto(bytes, headers);
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
