package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@RestController
@RequestMapping("api/security")
public class SecurityController {

    private final JwtSecurityProperties.JwtSecurityTokenProperties jwtSecurityTokenProperties;

    private final UserService userService;

    @ApiOperation(value = "Login")
    @PostMapping("login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
    }

    @ApiOperation(value = "Logout")
    @PostMapping("logout")
    public void logout(HttpServletRequest request) {
        String tokenValue = request.getHeader(jwtSecurityTokenProperties.getHeaderName());
        String token = null;
        if (tokenValue != null && tokenValue.startsWith(jwtSecurityTokenProperties.getPrefix())) {
            token = tokenValue.replace(jwtSecurityTokenProperties.getPrefix(), "").trim();
        }
        userService.logout(token);
    }

}