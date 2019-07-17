package com.github.rbaul.spring.boot.security.web.controllers.validators;

import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.repository.PrivilegeRepository;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.web.dtos.RoleUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class RoleUpdateRequestDtoValidator implements Validator {

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleUpdateRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(Object target, Errors errors) {
        RoleUpdateRequestDto roleUpdateRequestDto = (RoleUpdateRequestDto) target;

        validateUniqueRoleName(roleUpdateRequestDto, errors);

        validatePrivilegeExists(roleUpdateRequestDto, errors);
    }

    /**
     * Validate privilege exists
     */
    private void validatePrivilegeExists(RoleUpdateRequestDto roleUpdateRequestDto, Errors errors) {
        roleUpdateRequestDto.getPrivilegeIds().forEach(privilegeId -> {
            if (!privilegeRepository.existsById(privilegeId)) {
                errors.rejectValue(RoleUpdateRequestDto.Fields.privilegeIds, "Privilege with id " + privilegeId + " not exist");
            }
        });
    }

    /**
     * Validate unique role name
     */
    private void validateUniqueRoleName(RoleUpdateRequestDto roleUpdateRequestDto, Errors errors) {
        Optional<Role> roleOptional = roleRepository.findByName(roleUpdateRequestDto.getName());
        boolean roleNameExist = roleRepository.existsByName(roleUpdateRequestDto.getName());
        if (roleOptional.isPresent() && Objects.equals(roleOptional.get().getId(), roleUpdateRequestDto.getId())) {
            errors.rejectValue(RoleUpdateRequestDto.Fields.name, "Role already exist with same name: " + roleUpdateRequestDto.getName());
        }
    }
}
