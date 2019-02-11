package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import com.github.rbaul.spring.boot.security.web.dtos.LoginResponseDto;
import com.github.rbaul.spring.boot.security.web.dtos.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username  username
     * @param password  password
     * @return Optional of the Java Web Token, empty otherwise
     */
    public Optional<LoginResponseDto> signin(String username, String password) {
        log.info("New user attempting to sign in");
        Optional<LoginResponseDto> loginResponse = Optional.empty();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                String token = jwtProvider.createToken(username, user.get().getRoles());
                loginResponse = Optional.of(LoginResponseDto.builder()
                        .username(username)
                        .isAuthenticated(true)
                        .bearerToken(token).build());
            } catch (AuthenticationException e){
                log.info("Log in failed for user {}", username);
            }
        }
        return loginResponse;
    }

    /**
     * Create a new user in the database.
     *
     * @param signUpDto singup information
     * @return Optional of user, empty if the user already exists.
     */
    public Optional<User> create(SignUpDto signUpDto) {
        log.info("New user attempting to sign in");
        Optional<User> user = Optional.empty();
        if (!userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            Role role = roleRepository.findByRoleName(signUpDto.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not present"));
            user = Optional.of(userRepository.save(User.builder()
                    .username(signUpDto.getUsername())
                    .password(passwordEncoder.encode(signUpDto.getPassword()))
                    .roles(Collections.singletonList(role))
                    .firstName(signUpDto.getFirstName())
                    .lastName(signUpDto.getLastName()).build()));
        }
        return user;
    }

    /**
     * Create a new user in the database.
     *
     * @param userId User ID
     * @param signUpDto singup information
     * @return Optional of user, empty if the user already exists.
     */
    public User update(long userId, SignUpDto signUpDto) {
//        Optional<User> user = Optional.empty();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not Found"));
        Role role = roleRepository.findByRoleName(signUpDto.getRole())
                .orElseThrow(() -> new RuntimeException("Role not present"));

        user.setUsername(signUpDto.getUsername());
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setRoles(Collections.singletonList(role));
        user.setFirstName(signUpDto.getFirstName());
        return user;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(long userId) {
        return userRepository.findById(userId);
    }
}