package com.github.rbaul.spring.boot.security.web.handlers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Handle all Exceptions in for All controllers
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class JwtSecurityExceptionHandlers {

    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                errorAttributes.remove("exception");
                return errorAttributes;
            }
        };
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException ex, HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public void handleHttpServerErrorException(HttpServerErrorException ex, HttpServletResponse res) throws IOException {
        res.sendError(ex.getStatusCode().value(),ex.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public void handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, HttpServletResponse res) throws IOException {
        log.error("Handled Insufficient Authentication Exception", ex);
        res.sendError(HttpStatus.FORBIDDEN.value(), "Insufficient Authentication");
    }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex, HttpServletResponse res) throws IOException {
        log.error("Handled Internal Error Exception", ex);
        res.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong");
    }

}

