package com.github.rbaul.spring.boot.security.config;

import com.github.rbaul.spring.boot.security.JwtSecurityAutoConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = JwtSecurityAutoConfiguration.class)
@EnableConfigurationProperties({JwtSecurityProperties.class,
        JwtSecurityProperties.JwtSecurityTokenProperties.class,
        JwtSecurityProperties.JwtSecurityRoleProperties.class,
        JwtSecurityProperties.JwtSecurityUserProperties.class,
        JwtSecurityProperties.JwtSecurityAdministratorUserProperties.class
})
public class JwtSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}