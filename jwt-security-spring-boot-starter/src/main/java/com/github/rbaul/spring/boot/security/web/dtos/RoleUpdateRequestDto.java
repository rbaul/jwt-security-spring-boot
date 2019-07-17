package com.github.rbaul.spring.boot.security.web.dtos;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
public class RoleUpdateRequestDto {

    private Long id;

    @NotEmpty
    private String name;

    private String description;

    @Valid
    @NotEmpty
    private Set<@NotNull Long> privilegeIds;
}