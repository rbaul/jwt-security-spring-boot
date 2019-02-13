package com.github.rbaul.spring.boot.security.config;

import com.github.rbaul.spring.boot.security.JwtSecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = JwtSecurityAutoConfiguration.class)
@EnableConfigurationProperties({JwtSecurityProperties.class,
        JwtSecurityProperties.JwtSecurityTokenProperties.class,
        JwtSecurityProperties.JwtSecurityRoleProperties.class,
        JwtSecurityProperties.JwtSecurityUserProperties.class})
public class JwtSecurityConfiguration {
}