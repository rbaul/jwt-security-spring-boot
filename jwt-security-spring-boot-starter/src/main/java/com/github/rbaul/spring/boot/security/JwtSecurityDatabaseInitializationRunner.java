package com.github.rbaul.spring.boot.security;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class JwtSecurityDatabaseInitializationRunner implements ApplicationRunner {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtSecurityProperties jwtSecurityProperties;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() == 0 && roleRepository.count() == 0) { // create on first run
            List<Role> roles = roleRepository.saveAll(jwtSecurityProperties.getRoles().stream()
                    .map(role -> Role.builder()
                            .roleName(role.getName())
                            .description(role.getDescription()).build())
                    .collect(Collectors.toList())
            );

            log.info("Created roles in database: {}", roles);

            List<User> users = userRepository.saveAll(jwtSecurityProperties.getUsers().stream()
                    .map(jwtSecurityUserProperties -> {
                        List<Role> rolesForUser = roles.stream()
                                .filter(role -> jwtSecurityUserProperties.getRoles().contains(role.getRoleName()))
                                .collect(Collectors.toList());
                        return User.builder()
                                .username(jwtSecurityUserProperties.getUsername())
                                .password(passwordEncoder.encode(jwtSecurityUserProperties.getPassword()))
                                .roles(rolesForUser).build();
                    }).collect(Collectors.toList())
            );

            log.info("Created users in database: {}", users);
        }
    }
}