package com.github.rbaul.spring.boot.security.web.dtos;

import com.github.rbaul.spring.boot.activity_log.objects.ActivityLogStatus;
import com.github.rbaul.spring.boot.security.domain.model.types.ProductState;
import com.github.rbaul.spring.boot.security.web.ValidationGroups;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.Null;
import java.util.Date;

@ApiModel("Product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ActivityLogDto {

    private Long id;

    private String username;

    private String action;

    private Date time;

    private ActivityLogStatus status;
}
