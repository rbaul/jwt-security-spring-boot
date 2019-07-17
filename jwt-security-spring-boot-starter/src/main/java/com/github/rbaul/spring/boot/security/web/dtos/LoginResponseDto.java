package com.github.rbaul.spring.boot.security.web.dtos;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginResponseDto {
    private String username;
    private String bearerToken;
    private boolean isAuthenticated;
    private List<String> claims;
    private Date expiredAt;
}
