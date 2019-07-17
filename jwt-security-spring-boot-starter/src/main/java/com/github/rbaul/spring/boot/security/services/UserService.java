package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final DefaultUserDetailsService defaultUserDetailsService;

    private final ModelMapper modelMapper;

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username username
     * @param password password
     * @return Optional of the Java Web Token, empty otherwise
     */
    @Transactional(readOnly = true)
    public LoginResponseDto signin(String username, String password) {
        log.info("Username '{}' attempting to sign in", username);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found user with username: " + username, 1));
        List<String> privileges = defaultUserDetailsService.getPrivileges(user.getRoles());
        List<GrantedAuthority> grantedAuthorities = defaultUserDetailsService.getGrantedAuthorities(privileges);
        String token = jwtProvider.createToken(username, grantedAuthorities);
        Date expirationTime = jwtProvider.getExpirationTime(token);
        return LoginResponseDto.builder()
                .username(username)
                .isAuthenticated(true)
                .bearerToken(token)
                .claims(privileges)
                .expiredAt(expirationTime).build();
    }

    /**
     * Create a new user in the database.
     *
     * @param userCreateRequestDto singup information
     */
    @Transactional(readOnly = true)
    public UserResponseDto create(UserCreateRequestDto userCreateRequestDto) {
        User newUser = modelMapper.map(userCreateRequestDto, User.class);
        Collection<Role> roles = roleRepository.findByIdIn(userCreateRequestDto.getRoleIds());
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(userCreateRequestDto.getPassword()));
        User user = userRepository.save(newUser);
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
        user.setFirstName(userUpdateRequestDto.getFirstName());

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
    public Page<UserResponseDto> getPageable(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(user -> modelMapper.map(user, UserResponseDto.class));
    }

    /**
     * Remove user
     */
    @Transactional
    public void deleteUser(long userId) {
        User user = getUserById(userId);
        user.getRoles().forEach(role -> role.removeUser(user));
        userRepository.deleteById(userId);
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found user with id: " + userId, 1));
    }
}