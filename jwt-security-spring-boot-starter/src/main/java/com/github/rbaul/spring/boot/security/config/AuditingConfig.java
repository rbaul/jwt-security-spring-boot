package com.github.rbaul.spring.boot.security.config;

import com.github.rbaul.spring.boot.security.services.utils.SessionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return SessionUtils::getCurrentUsername;
    }
}