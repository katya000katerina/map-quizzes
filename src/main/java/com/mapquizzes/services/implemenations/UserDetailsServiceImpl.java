package com.mapquizzes.services.implemenations;

import com.mapquizzes.models.entities.UserEntity;
import com.mapquizzes.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOptional = userRepo.findByUsername(username);
        if (userOptional.isPresent()){
            UserEntity userEntity = userOptional.get();
            return User.builder()
                    .username(username)
                    .password(userEntity.getPassword())
                    .build();
        } else throw new UsernameNotFoundException(String.format("User with username \"%s\" not found", username));
    }
}
