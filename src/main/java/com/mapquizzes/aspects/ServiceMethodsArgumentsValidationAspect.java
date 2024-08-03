package com.mapquizzes.aspects;

import com.mapquizzes.exceptions.custom.badrequest.InvalidIdException;
import com.mapquizzes.exceptions.custom.internalservererror.NullDtoException;
import com.mapquizzes.exceptions.custom.internalservererror.NullEntityException;
import com.mapquizzes.exceptions.custom.internalservererror.NullIdException;
import com.mapquizzes.exceptions.custom.internalservererror.NullServiceMethodArgumentException;
import com.mapquizzes.services.interfaces.EntityExistenceService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ServiceMethodsArgumentsValidationAspect {
    private final EntityExistenceService entityExistenceService;

    @Before("execution(* com.mapquizzes.services.implemenations.*.*(..))")
    public void validateMethodArguments(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Class<?>[] paramTypes = signature.getParameterTypes();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            String paramName = paramNames[i];
            Class<?> paramType = paramTypes[i];

            validateArgumentNotNull(arg, paramName, paramType);

            if (paramName.endsWith("Id") && paramType.equals(Integer.class)) {
                validateEntityExists(paramName, (Integer) arg);
            }
        }
    }

    private void validateArgumentNotNull(Object arg, String paramName, Class<?> paramType) {
        String paramTypeName = paramType.getSimpleName();
        if (arg == null) {
            if (paramName.endsWith("Id")) {
                throw new NullIdException(capitalizeFirstLetter(paramName) + " is null");
            } else if (paramTypeName.endsWith("Dto")) {
                throw new NullDtoException(paramTypeName + " is null");
            } else if (paramTypeName.endsWith("Entity")) {
                throw new NullEntityException(paramTypeName + " is null");
            } else {
                throw new NullServiceMethodArgumentException(
                        String.format("%s of type %s is null",
                                capitalizeFirstLetter(paramName),
                                paramTypeName)
                );
            }
        }
    }

    private void validateEntityExists(String paramName, Integer id) {
        String entityName = paramName.replace("Id", "");
        boolean exists = entityExistenceService.checkIfEntityExists(entityName, id);
        if (!exists) {
            throw new InvalidIdException(
                    String.format("%s with id=%d doesn't exist",
                            capitalizeFirstLetter(entityName), id));
        }
    }

    private String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
