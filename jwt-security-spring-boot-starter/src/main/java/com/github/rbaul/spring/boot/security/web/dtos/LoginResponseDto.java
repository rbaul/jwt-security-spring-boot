package com.github.rbaul.spring.boot.security.web.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginResponseDto {
    private String bearerToken;
    private boolean isAuthenticated;
    private Date expiredAt;
}
