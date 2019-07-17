package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.services.RoleService;
import com.github.rbaul.spring.boot.security.web.controllers.validators.RoleCreateRequestDtoValidator;
import com.github.rbaul.spring.boot.security.web.controllers.validators.RoleUpdateRequestDtoValidator;
import com.github.rbaul.spring.boot.security.web.dtos.RoleCreateRequestDto;
import com.github.rbaul.spring.boot.security.web.dtos.RoleResponseDto;
import com.github.rbaul.spring.boot.security.web.dtos.RoleUpdateRequestDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@RestController
@PreAuthorize("hasRole(@administratorService.getPrivilegeName())")
@RequestMapping("api/roles")
public class RoleController {

    private final RoleService roleService;

    private final RoleCreateRequestDtoValidator roleCreateRequestDtoValidator;

    private final RoleUpdateRequestDtoValidator roleUpdateRequestDtoValidator;

    @InitBinder("roleCreateRequestDto")
    public void setupBinderRoleCreateRequestDto(WebDataBinder binder) {
        binder.addValidators(roleCreateRequestDtoValidator);
    }

    @ApiOperation(value = "Get all roles")
    @GetMapping
    public List<RoleResponseDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    @ApiOperation(value = "Get Role by ID")
    @GetMapping("{roleId}")
    public RoleResponseDto getRole(@PathVariable long roleId) {
        return roleService.getRole(roleId);
    }

    @ApiOperation(value = "Delete Role")
    @DeleteMapping("{roleId}")
    public void deleteRole(@PathVariable long roleId) {
        roleService.deleteRole(roleId);
    }

    @ApiOperation(value = "Update Role")
    @PutMapping("{roleId}")
    public RoleResponseDto updateRole(@PathVariable long roleId, @RequestBody @Validated RoleUpdateRequestDto roleUpdateRequestDto, BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {
        roleUpdateRequestDto.setId(roleId);
        roleUpdateRequestDtoValidator.validate(roleUpdateRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            MethodParameter roleUpdateRequestDtoParameter = new MethodParameter(this.getClass().getDeclaredMethod("updateRole", RoleUpdateRequestDto.class), 1);
            throw new MethodArgumentNotValidException(roleUpdateRequestDtoParameter, bindingResult);
        }
        return roleService.updateRole(roleId, roleUpdateRequestDto);
    }

    @ApiOperation(value = "Create Role")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RoleResponseDto createRole(@RequestBody @Validated RoleCreateRequestDto roleCreateRequestDto) {
        return roleService.createRole(roleCreateRequestDto);
    }


}