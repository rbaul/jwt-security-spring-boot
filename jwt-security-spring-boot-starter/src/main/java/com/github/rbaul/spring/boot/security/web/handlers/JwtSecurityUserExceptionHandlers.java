package com.github.rbaul.spring.boot.security.web.handlers;

import com.github.rbaul.spring.boot.security.services.exceptions.UserException;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handle Users Exceptions
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class JwtSecurityUserExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserException.class)
    public ErrorDto handleUserException(UserException ex) {
        return ErrorDto.builder()
                .errorCode(ex.getMessage()).build();
    }

}

