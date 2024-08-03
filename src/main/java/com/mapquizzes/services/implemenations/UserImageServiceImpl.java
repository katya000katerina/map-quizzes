package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.custom.badrequest.EmptyFileException;
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
import org.springframework.transaction.annotation.Transactional;
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
        if (file.isEmpty()) {
            throw new EmptyFileException("Uploaded image is empty");
        }

        UserImageEntity imageEntity = new UserImageEntity();

        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        imageEntity.setUser(userEntity);

        try {
            imageEntity.setBytes(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileName = file.getOriginalFilename();
        imageEntity.setFileName(fileName);
        imageRepo.getIdByUser(userEntity).ifPresent(imageEntity::setId);

        byte[] bytes = imageRepo.save(imageEntity).getBytes();

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

    @Transactional
    @Override
    public void deleteByUser(UserEntity userEntity) {
        imageRepo.deleteByUser(userEntity);
    }

    private <R> R getPrincipalImage(Principal principal, BiFunction<String, byte[], R> func) {
        UserEntity userEntity = userService.getEntityByPrincipal(principal);
        Optional<UserImageEntity> imageOptional = imageRepo.findByUser(userEntity);

        if (imageOptional.isPresent()) {
            UserImageEntity imageEntity = imageOptional.get();
            return func.apply(imageEntity.getFileName(), imageEntity.getBytes());
        } else {
            try {
                File imageFile = ResourceUtils.getFile(noImagePath);
                byte[] bytes = Files.readAllBytes(imageFile.toPath());
                return func.apply(imageFile.getName(), bytes);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private PrincipalProfileImageDto getPrincipalProfileImageDto(String fileName, byte[] bytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((getContentType(fileName)));
        headers.setContentLength(bytes.length);
        return new PrincipalProfileImageDto(bytes, headers);
    }

    private MediaType getContentType(String fileName) {
        if (fileName.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        }
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
