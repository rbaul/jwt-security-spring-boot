package com.github.rbaul.spring.boot.security.web.controllers.validators;

import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.domain.repository.UserRepository;
import com.github.rbaul.spring.boot.security.web.dtos.UserUpdateRequestDto;
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
public class UserUpdateRequestDtoValidator implements Validator {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserUpdateRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    @Transactional(readOnly = true)
    public void validate(Object target, Errors errors) {
        UserUpdateRequestDto userUpdateRequestDto = (UserUpdateRequestDto) target;

        validateUniqueUserName(userUpdateRequestDto, errors);

        validateRolesExists(userUpdateRequestDto, errors);
    }

    /**
     * Validate role exists
     */
    private void validateRolesExists(UserUpdateRequestDto userUpdateRequestDto, Errors errors) {
        userUpdateRequestDto.getRoleIds().forEach(roleId -> {
            if (!roleRepository.existsById(roleId)) {
                errors.rejectValue(UserUpdateRequestDto.Fields.roleIds, "Role with id " + roleId + " not exist");
            }
        });
    }

    /**
     * Validate unique user
     */
    private void validateUniqueUserName(UserUpdateRequestDto userUpdateRequestDto, Errors errors) {
        Optional<User> userOptional = userRepository.findByUsername(userUpdateRequestDto.getUsername());
        if (userOptional.isPresent() && !Objects.equals(userOptional.get().getId(), userUpdateRequestDto.getId())) {
            errors.rejectValue(UserUpdateRequestDto.Fields.username, "User already exist with same username: " + userUpdateRequestDto.getUsername());
        }
    }
}
