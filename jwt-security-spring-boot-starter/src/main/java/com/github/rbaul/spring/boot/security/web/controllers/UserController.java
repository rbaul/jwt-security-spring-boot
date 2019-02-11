package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.services.UserService;
import com.github.rbaul.spring.boot.security.web.dtos.SignUpDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @Getter
    @Value("${security.jwt.api-user-roles.crud:ROLE_ADMIN}")
    private String roleCrud;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid SignUpDto signUpDto){
        return userService.create(signUpDto)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST,"User already exists"));
    }

    @PutMapping("{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public User update(@PathVariable long userId, @RequestBody @Valid SignUpDto signUpDto){
        return userService.update(userId, signUpDto);
    }

    @GetMapping
    @PreAuthorize("hasRole(@userController.getRoleCrud())")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("{userId}")
    @PreAuthorize("hasRole(@userController.getRoleCrud())")
    public User getUser(@PathVariable long userId) {
        return userService.getUser(userId)
                .orElseThrow(() -> new HttpServerErrorException(HttpStatus.BAD_REQUEST,"User not exists"));
    }

}