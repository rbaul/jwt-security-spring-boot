package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.services.UserService;
import com.github.rbaul.spring.boot.security.web.controllers.validators.UserCreateRequestDtoValidator;
import com.github.rbaul.spring.boot.security.web.controllers.validators.UserUpdateRequestDtoValidator;
import com.github.rbaul.spring.boot.security.web.dtos.UserCreateRequestDto;
import com.github.rbaul.spring.boot.security.web.dtos.UserResponseDto;
import com.github.rbaul.spring.boot.security.web.dtos.UserUpdateRequestDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@RestController
@PreAuthorize("hasRole(@administratorService.getPrivilegeName())")
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    private final UserCreateRequestDtoValidator userCreateRequestDtoValidator;

    private final UserUpdateRequestDtoValidator userUpdateRequestDtoValidator;

    @InitBinder("userCreateRequestDto")
    public void setupBinderUserCreateRequestDto(WebDataBinder binder) {
        binder.addValidators(userCreateRequestDtoValidator);
    }

    @ApiOperation(value = "Create user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(@RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
        return userService.create(userCreateRequestDto);
    }

    @ApiOperation(value = "Update user")
    @PutMapping("{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto updateUser(@PathVariable long userId, @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto, BindingResult bindingResult) throws NoSuchMethodException, MethodArgumentNotValidException {
        userUpdateRequestDto.setId(userId);
        userUpdateRequestDtoValidator.validate(userUpdateRequestDto, bindingResult);
        if (bindingResult.hasErrors()) {
            MethodParameter userUpdateRequestDtoParameter = new MethodParameter(this.getClass().getDeclaredMethod("updateUser", UserUpdateRequestDto.class), 1);
            throw new MethodArgumentNotValidException(userUpdateRequestDtoParameter, bindingResult);
        }
        return userService.update(userId, userUpdateRequestDto);
    }

    @ApiOperation(value = "Get all users")
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAll();
    }

    @ApiOperation(value = "Fetch all users with paging")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully lists products")})
    @GetMapping("pageable")
    public Page<UserResponseDto> getPageUsers(@PageableDefault @ApiIgnore(
            "Ignored because swagger ui shows the wrong params, " +
                    "instead they are explained in the implicit params"
    ) Pageable pageable) {
        return userService.getPageable(pageable);
    }

    @ApiOperation(value = "Get user")
    @GetMapping("{userId}")
    public UserResponseDto getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @ApiOperation(value = "Delete user")
    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

}