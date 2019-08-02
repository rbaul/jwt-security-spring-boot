package com.github.rbaul.spring.boot.security.web.dtos;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponseDto {
    private long id;

    private String username;

    private String firstName;

    private String lastName;

    private Collection<RoleResponseDto> roles;
}
