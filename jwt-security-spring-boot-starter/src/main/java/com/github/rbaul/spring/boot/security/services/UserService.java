package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import com.github.rbaul.spring.boot.security.services.exceptions.UserException;
import com.github.rbaul.spring.boot.security.services.utils.SessionUtils;
import com.github.rbaul.spring.boot.security.web.dtos.LoginResponseDto;
import com.github.rbaul.spring.boot.security.web.dtos.UserCreateRequestDto;
import com.github.rbaul.spring.boot.security.web.dtos.UserResponseDto;
import com.github.rbaul.spring.boot.security.web.dtos.UserUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserService {

    private final UserRepository userRepository;

    private final SessionService sessionService;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final DefaultUserDetailsService defaultUserDetailsService;

    private final ModelMapper modelMapper;

    private final JwtSecurityProperties.JwtSecurityAdministratorUserProperties administratorUserProperties;

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username username
     * @param password password
     * @return Optional of the Java Web Token, empty otherwise
     */
    @Transactional
    public LoginResponseDto login(String username, String password) {
        log.info("Username '{}' attempting to sign in", username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found user with username: " + username, 1));
        Set<String> privilegeNames = user.getPrivilegeNames();
        Set<String> roleNames = user.getRoleNames();
        String token = jwtProvider.createToken(username, roleNames, privilegeNames);
        Date expirationTime = jwtProvider.getExpirationTime(token);

        // Update session
        sessionService.updateSession(token);

        return LoginResponseDto.builder()
                .isAuthenticated(true)
                .bearerToken(token)
                .expiredAt(expirationTime)
                .build();
    }

    /**
     * Create a new user in the database.
     *
     * @param userCreateRequestDto singup information
     */
    @Transactional
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        User newUser = modelMapper.map(userCreateRequestDto, User.class);
        Collection<Role> roles = roleRepository.findByIdIn(userCreateRequestDto.getRoleIds());
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(userCreateRequestDto.getPassword()));
        User user = userRepository.save(newUser);
        sessionService.createSession(userCreateRequestDto.getUsername());
        return modelMapper.map(user, UserResponseDto.class);
    }

    /**
     * Update a user in the database.
     *
     * @param userId               User ID
     * @param userUpdateRequestDto singup information
     */
    @Transactional
    public UserResponseDto update(long userId, UserUpdateRequestDto userUpdateRequestDto) {
        User user = getUserById(userId);
        Collection<Role> roles = roleRepository.findByIdIn(userUpdateRequestDto.getRoleIds());

        user.setUsername(userUpdateRequestDto.getUsername());
        user.setFirstName(userUpdateRequestDto.getFirstName());
        user.setLastName(userUpdateRequestDto.getLastName());
        user.setPassword(passwordEncoder.encode(userUpdateRequestDto.getPassword()));
        user.setRoles(roles);
        sessionService.logoutSession(userUpdateRequestDto.getUsername());
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAll() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUser(long userId) {
        return modelMapper.map(getUserById(userId), UserResponseDto.class);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> search(String filter, Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(UserRepository.filterEveryWay(filter), pageable);
        return usersPage.map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    /**
     * Remove user
     */
    @Transactional
    public void deleteUser(long userId) {
        User user = getUserById(userId);

        validateDeleteUser(user);

        user.getRoles().forEach(role -> role.removeUser(user));
        userRepository.deleteById(userId);
    }

    /**
     * Validate delete user
     */
    private void validateDeleteUser(User user) {
        String currentUsername = SessionUtils.getCurrentUsername().orElse(null);
        if (Objects.equals(currentUsername, user.getUsername())) {
            throw new UserException("Can't delete self user");
        }

        String administratorPrivilegeName = administratorUserProperties.getPrivilegeName();
        boolean containsAdministratorPrivilegeName = user.getPrivilegeNames().contains(administratorPrivilegeName);

        if (containsAdministratorPrivilegeName) {
            long countOfAdminUsers = userRepository.countByRoles_privileges_name(administratorPrivilegeName);
            if (countOfAdminUsers == 1)
                throw new UserException("User with administrator privilege must be no less than one");
        }
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found user with id: " + userId, 1));
    }

    /**
     * Logout
     *
     * @param token - JWT token
     */
    @Transactional
    public void logout(String token) {
        defaultUserDetailsService.loadUserByJwtToken(token)
                .ifPresent(userDetails -> sessionService.logoutSession(userDetails.getUsername()));
    }
}