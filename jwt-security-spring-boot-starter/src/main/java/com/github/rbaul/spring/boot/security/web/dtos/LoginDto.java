package com.github.rbaul.spring.boot.security.web.dtos;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginDto {
    @NotEmpty
    private String username;

    @NotNull
    private String password;
}
