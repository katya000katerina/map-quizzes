package com.mapquizzes.validation.validators;

import com.mapquizzes.repositories.UserRepository;
import com.mapquizzes.validation.annotations.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserRepository userRepo;

    @Override
    public void initialize(UniqueUsername constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return !userRepo.existsByUsername(value);
    }
}
