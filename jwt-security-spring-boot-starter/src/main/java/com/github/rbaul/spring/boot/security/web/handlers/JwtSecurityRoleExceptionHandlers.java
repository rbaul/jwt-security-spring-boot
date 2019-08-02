package com.github.rbaul.spring.boot.security.web.handlers;

import com.github.rbaul.spring.boot.security.services.exceptions.RoleException;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ErrorDto;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ErrorSecurityCodes;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ValidationErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handle Roles Exceptions
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class JwtSecurityRoleExceptionHandlers {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RoleException.class)
    public ErrorDto handleAccessDeniedException(RoleException ex) {
        return ErrorDto.builder()
                .errorCode(ex.getMessage()).build();
    }

}

