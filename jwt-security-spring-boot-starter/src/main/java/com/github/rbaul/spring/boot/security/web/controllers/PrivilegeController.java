package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.services.PrivilegeService;
import com.github.rbaul.spring.boot.security.web.dtos.PrivilegeResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}