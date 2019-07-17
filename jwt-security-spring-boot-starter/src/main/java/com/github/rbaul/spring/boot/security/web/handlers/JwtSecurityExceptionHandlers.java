package com.github.rbaul.spring.boot.security.web.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Handle all Exceptions in for All controllers
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class JwtSecurityExceptionHandlers {

//    @Bean
//    public ErrorAttributes errorAttributes() {
//        // Hide exception field in the return object
//        return new DefaultErrorAttributes() {
//
//            @Override
//            public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
//                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
//                errorAttributes.remove("exception");
//                return errorAttributes;
//            }
//        };
//    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public void handleHttpServerErrorException(HttpServerErrorException ex, HttpServletResponse res) throws IOException {
        res.sendError(ex.getStatusCode().value(), ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public String handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        log.error("Handled Insufficient Authentication Exception", ex);
        return "Insufficient Authentication";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        log.error("Handled Internal Error Exception", ex);
        return "Something went wrong";
    }

}

