package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.config.ApiImplicitPageable;
import com.github.rbaul.spring.boot.security.services.impl.ActivityLogServiceImpl;
import com.github.rbaul.spring.boot.security.web.dtos.ActivityLogDto;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ErrorDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("api/activity-logs")
public class ActivityLogController {

    private final ActivityLogServiceImpl activityLogService;

    @ApiOperation(value = "Get Activity Log")
    @ApiResponses({@ApiResponse(code = 200, message = "Retrieved Activity Log"),
            @ApiResponse(code = 404, message = "Activity Log Not Found", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @Secured({"ROLE_ACTIVITY_PRIVILEGE"})
    @GetMapping("{activityLogId}")
    public ActivityLogDto get(@PathVariable long activityLogId) {
        return activityLogService.get(activityLogId);
    }

    @ApiOperation(value = "Delete Activity Log")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully Activity Log deleted"),
            @ApiResponse(code = 428, message = "Invalid Activity Log Id", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @Secured({"ROLE_ACTIVITY_PRIVILEGE"})
    @DeleteMapping("{activityLogId}")
    public void delete(@PathVariable long activityLogId) {
        activityLogService.delete(activityLogId);
    }

    @ApiOperation(value = "Fetch all activity logs with paging")
    @ApiImplicitPageable
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully lists activity logs")})
    @Secured({"ROLE_ACTIVITY_PRIVILEGE"})
    @GetMapping
    public Page<ActivityLogDto> fetch(@PageableDefault @ApiIgnore(
            "Ignored because swagger ui shows the wrong params, " +
                    "instead they are explained in the implicit params"
    ) Pageable pageable) {
        return activityLogService.getPageable(pageable);
    }

}
