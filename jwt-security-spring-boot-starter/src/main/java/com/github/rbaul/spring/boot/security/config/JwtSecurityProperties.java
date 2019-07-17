package com.github.rbaul.spring.boot.security.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtSecurityProperties {

    private JwtSecurityAdministratorUserProperties administrator = new JwtSecurityAdministratorUserProperties();

    private JwtSecurityTokenProperties token = new JwtSecurityTokenProperties();

    private List<JwtSecurityUserProperties> users = new ArrayList<>();

    private List<JwtSecurityPrivilegeProperties> privileges = new ArrayList<>();

    private List<JwtSecurityRoleProperties> roles = new ArrayList<>();

    @PostConstruct
    public void createAdministratorUser() {
        users.add(JwtSecurityUserProperties.builder()
                .firstName(administrator.getFirstName())
                .lastName(administrator.getLastName())
                .username(administrator.getUsername())
                .password(administrator.getPassword())
                .roles(Collections.singletonList(administrator.getRoleName())).build());
        privileges.add(JwtSecurityPrivilegeProperties.builder()
                .name(administrator.getPrivilegeName())
                .description(administrator.getPrivilegeDescription()).build());
        roles.add(JwtSecurityRoleProperties.builder()
                .name(administrator.getRoleName())
                .description(administrator.getRoleDescription())
                .privileges(privileges.stream().map(JwtSecurityPrivilegeProperties::getName).collect(Collectors.toList())).build());

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ConfigurationProperties(prefix = "security.jwt.token")
    @EnableConfigurationProperties
    public static class JwtSecurityTokenProperties {
        private String rolesKey = "roles";
        private String secretKey = "secret-key-for-encryption";
        private long expiration = 600000; // 10 minutes
        private String headerName = "Authorization";
        private int passwordStrength = 12;
        private String prefix = "Bearer";
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ConfigurationProperties(prefix = "security.jwt.roles")
    @EnableConfigurationProperties
    public static class JwtSecurityRoleProperties {
        private String name;
        private String description;
        private List<String> privileges;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ConfigurationProperties(prefix = "security.jwt.privileges")
    @EnableConfigurationProperties
    public static class JwtSecurityPrivilegeProperties {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ConfigurationProperties(prefix = "security.jwt.users")
    @EnableConfigurationProperties
    public static class JwtSecurityUserProperties {
        private String firstName;
        private String lastName;
        private String username;
        private String password;
        private List<String> roles;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ConfigurationProperties(prefix = "security.jwt.administrator")
    @EnableConfigurationProperties
    public static class JwtSecurityAdministratorUserProperties {
        private String firstName;
        private String lastName;
        private String username = "admin";
        private String password = "admin";
        private String roleName = "Administrator";
        private String roleDescription = "";
        private String privilegeName = "ROLE_ADMIN_PRIVILEGE";
        private String privilegeDescription = "";
    }

}
