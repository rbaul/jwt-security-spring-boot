package com.github.rbaul.spring.boot.security.web.controllers.validators;

import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import com.github.rbaul.spring.boot.security.web.dtos.UserCreateRequestDto;
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
public class UserCreateRequestDtoValidator implements Validator {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return UserCreateRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(Object target, Errors errors) {
        UserCreateRequestDto userCreateRequestDto = (UserCreateRequestDto) target;

        validateUniqueUserName(userCreateRequestDto, errors);

        validateRolesExists(userCreateRequestDto, errors);
    }

    /**
     * Validate role exists
     */
    private void validateRolesExists(UserCreateRequestDto userCreateRequestDto, Errors errors) {
        userCreateRequestDto.getRoleIds().forEach(roleId -> {
            if (!roleRepository.existsById(roleId)) {
                errors.rejectValue(UserCreateRequestDto.Fields.roleIds, "Role with id " + roleId + " not exist");
            }
        });
    }

    /**
     * Validate unique user
     */
    private void validateUniqueUserName(UserCreateRequestDto userCreateRequestDto, Errors errors) {
        boolean userNameExist = userRepository.existsByUsername(userCreateRequestDto.getUsername());
        if (userNameExist) {
            errors.rejectValue(UserCreateRequestDto.Fields.username, "User already exist with same username: " + userCreateRequestDto.getUsername());
        }
    }
}
