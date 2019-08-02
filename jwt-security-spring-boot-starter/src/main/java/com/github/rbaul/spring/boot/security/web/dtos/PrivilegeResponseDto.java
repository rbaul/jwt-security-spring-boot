package com.github.rbaul.spring.boot.security.web.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivilegeResponseDto {

    private long id;

    private String name;

    private String description;

}