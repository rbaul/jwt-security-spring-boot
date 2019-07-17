package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.config.JwtSecurityProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AdministratorService {
    @Getter
    private final String privilegeName;

    @Autowired
    public AdministratorService(JwtSecurityProperties.JwtSecurityAdministratorUserProperties jwtSecurityAdministratorUserProperties) {
        privilegeName = jwtSecurityAdministratorUserProperties.getPrivilegeName();
    }
}
