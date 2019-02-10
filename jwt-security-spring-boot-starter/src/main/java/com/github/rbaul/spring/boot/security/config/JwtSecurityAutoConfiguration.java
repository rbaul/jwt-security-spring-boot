package com.github.rbaul.spring.boot.security.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JwtSecurityProperties.class,
        JwtSecurityProperties.JwtSecurityTokenProperties.class,
        JwtSecurityProperties.JwtSecurityRoleProperties.class,
        JwtSecurityProperties.JwtSecurityUserProperties.class})
@ComponentScan("com.github.rbaul.spring.boot.security")
public class JwtSecurityAutoConfiguration {
}
