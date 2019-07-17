package com.github.rbaul.spring.boot.security.web.dtos;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponseDto {
    private long id;

    private String name;

    private String description;

    private Collection<PrivilegeResponseDto> privileges;
}