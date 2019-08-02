package com.github.rbaul.spring.boot.security.web.handlers;

import com.github.rbaul.spring.boot.security.web.dtos.errors.ErrorDto;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ErrorSecurityCodes;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ValidationErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handle Validation Exceptions
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class JwtSecurityValidationExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Set<ValidationErrorDto> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> ValidationErrorDto.builder()
                        .errorCode(err.getCode())
                        .fieldName(err.getField())
                        .rejectedValue(err.getRejectedValue())
                        .message(err.getDefaultMessage())
                        .build())
                .collect(Collectors.toSet());
        errors.addAll(ex.getBindingResult().getGlobalErrors().stream()
                .map(err -> ValidationErrorDto.builder()
                        .errorCode(err.getCode())
                        .message(err.getDefaultMessage())
                        .build())
                .collect(Collectors.toSet()));

        return ErrorDto.builder()
                .errorCode(ErrorSecurityCodes.DATA_VALIDATION.name())
                .errors(Collections.unmodifiableSet(errors))
                .message(ex.getLocalizedMessage())
                .build();
    }

}

