package com.github.rbaul.spring.boot.security;

import com.github.rbaul.spring.boot.security.config.JwtSecurityConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigurationPackage
@Import(JwtSecurityConfiguration.class)
public class JwtSecurityAutoConfiguration {
}
