package com.github.rbaul.spring.boot.security.services.exceptions;

public class PrivilegeNotFoundException extends RuntimeException {
    public PrivilegeNotFoundException(String message) {
        super(message);
    }

    public PrivilegeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
