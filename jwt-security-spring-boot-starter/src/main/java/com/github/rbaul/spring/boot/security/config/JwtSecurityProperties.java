package com.github.rbaul.spring.boot.security.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtSecurityProperties {

    private JwtSecurityTokenProperties token =  new JwtSecurityTokenProperties();

    private List<JwtSecurityUserProperties> users = Arrays.asList(
            new JwtSecurityUserProperties("admin", "admin", Collections.singletonList("ROLE_ADMIN")),
            new JwtSecurityUserProperties("user", "user", Collections.singletonList("ROLE_USER"))
    );

    private List<JwtSecurityRoleProperties> roles = Arrays.asList(
            new JwtSecurityRoleProperties("ROLE_ADMIN", "Administrator"),
            new JwtSecurityRoleProperties("ROLE_USER", "User")
    );

    @Getter
    @Setter
    @NoArgsConstructor
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
    @ConfigurationProperties(prefix = "security.jwt.roles")
    @EnableConfigurationProperties
    public static class JwtSecurityRoleProperties {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ConfigurationProperties(prefix = "security.jwt.users")
    @EnableConfigurationProperties
    public static class JwtSecurityUserProperties {
        private String username;
        private String password;
        private List<String> roles;
    }
}
