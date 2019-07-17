package com.github.rbaul.spring.boot.security.web.controllers.validators;

import com.github.rbaul.spring.boot.security.domain.repository.PrivilegeRepository;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.web.dtos.RoleCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class RoleCreateRequestDtoValidator implements Validator {

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleCreateRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(Object target, Errors errors) {
        RoleCreateRequestDto roleCreateRequestDto = (RoleCreateRequestDto) target;

        validateUniqueRoleName(roleCreateRequestDto, errors);

        validatePrivilegeExists(roleCreateRequestDto, errors);
    }

    /**
     * Validate privilege exists
     */
    private void validatePrivilegeExists(RoleCreateRequestDto roleCreateRequestDto, Errors errors) {
        roleCreateRequestDto.getPrivilegeIds().forEach(privilegeId -> {
            if (!privilegeRepository.existsById(privilegeId)) {
                errors.rejectValue(RoleCreateRequestDto.Fields.privilegeIds, "Privilege with id " + privilegeId + " not exist");
            }
        });
    }

    /**
     * Validate unique role name
     */
    private void validateUniqueRoleName(RoleCreateRequestDto roleCreateRequestDto, Errors errors) {
        boolean roleNameExist = roleRepository.existsByName(roleCreateRequestDto.getName());
        if (roleNameExist) {
            errors.rejectValue(RoleCreateRequestDto.Fields.name, "Role already exist with same name: " + roleCreateRequestDto.getName());
        }
    }
}
