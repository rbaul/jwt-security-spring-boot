package com.github.rbaul.spring.boot.security;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
import com.github.rbaul.spring.boot.security.domain.model.Privilege;
import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.PrivilegeRepository;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class JwtSecurityInitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtSecurityProperties jwtSecurityProperties;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        // Create all privileges
        jwtSecurityProperties.getPrivileges().forEach(privilege -> createPrivilegeIfNotFound(privilege.getName()));

        // Create all roles
        jwtSecurityProperties.getRoles().forEach(role -> {
            Set<Privilege> privileges = role.getPrivileges().stream().map(this::createPrivilegeIfNotFound).collect(Collectors.toSet());
            createRoleIfNotFound(role.getName(), privileges);
        });

        // Create all users
        jwtSecurityProperties.getUsers().stream()
                .filter(jwtSecurityUserProperties -> !userRepository.findByUsername(jwtSecurityUserProperties.getUsername()).isPresent())
                .forEach(jwtSecurityUserProperties -> {
                    Set<Role> userRoles = jwtSecurityUserProperties.getRoles().stream()
                            .map(roleRepository::findByName)
                            .filter(Optional::isPresent)
                            .map(Optional::get).collect(Collectors.toSet());
                    User user = User.builder()
                            .username(jwtSecurityUserProperties.getUsername())
                            .password(passwordEncoder.encode(jwtSecurityUserProperties.getPassword()))
                            .roles(userRoles).build();
                    userRepository.save(user);
                });

        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
        Optional<Privilege> privilegeOptional = privilegeRepository.findByName(name);
        if (privilegeOptional.isPresent()) {
            return privilegeOptional.get();

        } else {
            Privilege privilege = Privilege.builder().name(name).build();
            return privilegeRepository.save(privilege);
        }
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Optional<Role> roleOptional = roleRepository.findByName(name);
        if (roleOptional.isPresent()) {
            return roleOptional.get();
        } else {
            Role role = Role.builder()
                    .name(name)
                    .privileges(privileges).build();
            return roleRepository.save(role);
        }
    }
}