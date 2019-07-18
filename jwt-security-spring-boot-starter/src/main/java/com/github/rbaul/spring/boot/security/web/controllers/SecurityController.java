package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.services.UserService;
import com.github.rbaul.spring.boot.security.web.dtos.LoginRequestDto;
import com.github.rbaul.spring.boot.security.web.dtos.LoginResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@RestController
@RequestMapping("api/security")
public class SecurityController {

    private final UserService userService;

    @ApiOperation(value = "Login")
    @PostMapping("login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
    }

}