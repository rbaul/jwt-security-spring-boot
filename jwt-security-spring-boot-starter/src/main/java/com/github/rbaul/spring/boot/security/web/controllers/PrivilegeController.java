package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.services.PrivilegeService;
import com.github.rbaul.spring.boot.security.web.dtos.PrivilegeResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@RestController
@PreAuthorize("hasRole(@administratorService.getPrivilegeName())")
@RequestMapping("api/privileges")
public class PrivilegeController {

    private final PrivilegeService privilegeService;

    @ApiOperation(value = "Get privileges")
    @GetMapping
    public List<PrivilegeResponseDto> getPrivileges() {
        return privilegeService.getAllPrivileges();
    }

    @ApiOperation(value = "Get privilege by ID")
    @GetMapping("{privilegeId}")
    public PrivilegeResponseDto getPrivilege(long privilegeId) {
        return privilegeService.getPrivilege(privilegeId);
    }

    @ApiOperation(value = "Fetch all privileges with paging by filter")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully lists privileges")})
    @GetMapping("search")
    public Page<PrivilegeResponseDto> search(@RequestParam(required = false) String filter, @PageableDefault @ApiIgnore(
            "Ignored because swagger ui shows the wrong params, " +
                    "instead they are explained in the implicit params"
    ) Pageable pageable) {
        return privilegeService.search(filter, pageable);
    }
}