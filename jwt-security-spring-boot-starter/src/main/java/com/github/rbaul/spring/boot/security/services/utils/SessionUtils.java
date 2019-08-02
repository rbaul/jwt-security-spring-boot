package com.github.rbaul.spring.boot.security.services.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class SessionUtils {

    public static final String JSESSIONID = "JSESSIONID";

    public static Optional<String> getCookieValueOfSessionId() {
        return getCookieValueByName(JSESSIONID);
    }

    public static Optional<String> getCookieValueByName(String cookieName) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String value = null;
        if (requestAttributes != null) {
            Cookie[] cookies = requestAttributes.getRequest().getCookies();
            Optional<Cookie> jsessionidCookie = Arrays.stream(cookies).filter(cookie -> Objects.equals(cookie.getName(), cookieName)).findFirst();
            value = jsessionidCookie.map(Cookie::getValue).orElse(null);
        }
        return Optional.ofNullable(value);
    }

}
