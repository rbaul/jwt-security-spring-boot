package com.github.rbaul.spring.boot.security.web.dtos;

import com.github.rbaul.spring.boot.security.domain.model.types.ProductState;
import com.github.rbaul.spring.boot.security.web.ValidationGroups;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Null;

@ApiModel("Product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDto extends AuditableDto {

    @Null(groups = ValidationGroups.Create.class)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private ProductState state;
}
