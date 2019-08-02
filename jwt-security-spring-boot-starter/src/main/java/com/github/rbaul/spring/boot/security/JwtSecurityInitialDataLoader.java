package com.github.rbaul.spring.boot.security;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
import com.github.rbaul.spring.boot.security.domain.model.Privilege;
import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.Session;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.PrivilegeRepository;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.SessionRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class JwtSecurityInitialDataLoader implements ApplicationListener<ApplicationStartedEvent> {

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtSecurityProperties jwtSecurityProperties;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationStartedEvent event) {

        if (userRepository.count() == 0) { // create on first run
            log.debug("Performing population DB with default security data...");

            // Create all privileges
            log.info("Performing create all default privileges: '{}'", jwtSecurityProperties.getPrivileges().stream()
                    .map(JwtSecurityProperties.JwtSecurityPrivilegeProperties::getName)
                    .collect(Collectors.toList()));
            jwtSecurityProperties.getPrivileges().forEach(privilege -> createPrivilegeIfNotFound(privilege.getName()));

            // Create all roles
            log.info("Performing create all default roles: '{}'", jwtSecurityProperties.getRoles().stream()
                    .map(JwtSecurityProperties.JwtSecurityRoleProperties::getName)
                    .collect(Collectors.toList()));
            jwtSecurityProperties.getRoles().forEach(role -> {
                Set<Privilege> privileges = role.getPrivileges().stream().map(this::createPrivilegeIfNotFound).collect(Collectors.toSet());
                createRoleIfNotFound(role.getName(), privileges);
            });

            // Create all users
            log.info("Performing create all default users: '{}'", jwtSecurityProperties.getUsers().stream()
                    .map(JwtSecurityProperties.JwtSecurityUserProperties::getUsername)
                    .collect(Collectors.toList()));
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

                        // Create initial session for user
                        Session session = Session.builder()
                                .username(jwtSecurityUserProperties.getUsername())
                                .lastLogin(new Date())
                                .build();
                        sessionRepository.save(session);
                    });
            log.debug("Population DB with default security data finished");
        }
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