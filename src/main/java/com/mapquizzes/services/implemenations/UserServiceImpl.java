package com.mapquizzes.services.implemenations;

import com.mapquizzes.exceptions.EntityNotFoundException;
import com.mapquizzes.models.dto.UserDto;
import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.models.mapping.mappers.UserMapper;
import com.mapquizzes.repositories.interfaces.UserRepository;
import com.mapquizzes.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;
    private final UserMapper mapper;

    @Override
    public UserDto getByUsername(String username) {
        Optional<UserEntity> optional = userRepo.findByUsername(username);
        if (optional.isPresent()) {
            return mapper.mapEntityToDto(optional.get());
        } else throw new EntityNotFoundException(String.format("User with username \"%s\" not found", username));
    }
}
