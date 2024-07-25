package com.mapquizzes.validation.annotations;

import com.mapquizzes.validation.validators.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "The username is not available";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
