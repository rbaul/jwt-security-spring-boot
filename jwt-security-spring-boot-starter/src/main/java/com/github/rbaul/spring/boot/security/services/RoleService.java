package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.domain.model.Privilege;
import com.github.rbaul.spring.boot.security.domain.model.Role;
import com.github.rbaul.spring.boot.security.domain.model.User;
import com.github.rbaul.spring.boot.security.domain.repository.PrivilegeRepository;
import com.github.rbaul.spring.boot.security.domain.repository.RoleRepository;
import com.github.rbaul.spring.boot.security.services.exceptions.RoleException;
import com.github.rbaul.spring.boot.security.web.dtos.RoleCreateRequestDto;
import com.github.rbaul.spring.boot.security.web.dtos.RoleResponseDto;
import com.github.rbaul.spring.boot.security.web.dtos.RoleUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final PrivilegeRepository privilegeRepository;

    private final ModelMapper modelMapper;

    /**
     * Get All roles
     */
    @Transactional(readOnly = true)
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> modelMapper.map(role, RoleResponseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Create role
     */
    @Transactional
    public RoleResponseDto createRole(RoleCreateRequestDto roleCreateRequestDto) {
        Role newRole = modelMapper.map(roleCreateRequestDto, Role.class);
        Collection<Privilege> privileges = privilegeRepository.findByIdIn(roleCreateRequestDto.getPrivilegeIds());
        newRole.setPrivileges(privileges);
        Role role = roleRepository.save(newRole);
        return modelMapper.map(role, RoleResponseDto.class);
    }

    /**
     * Remove role
     */
    @Transactional
    public void deleteRole(long roleId) {
        Role role = getRoleById(roleId);
        if (!role.getUsers().isEmpty()) {
            throw new RoleException("Role in use by: " + role.getUsers().stream()
                    .map(User::getUsername)
                    .collect(Collectors.joining(",")));
        }
        role.getPrivileges().forEach(privilege -> privilege.removeRole(role));
        roleRepository.deleteById(roleId);
    }

    @Transactional(readOnly = true)
    public RoleResponseDto getRole(long roleId) {
        return modelMapper.map(getRoleById(roleId), RoleResponseDto.class);
    }

    private Role getRoleById(long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found role with id: " + roleId, 1));
    }

    /**
     * Update role
     */
    @Transactional
    public RoleResponseDto updateRole(long roleId, RoleUpdateRequestDto roleUpdateRequestDto) {
        Role role = getRoleById(roleId);
        role.setName(roleUpdateRequestDto.getName());
        role.setDescription(roleUpdateRequestDto.getDescription());
        Collection<Privilege> privileges = privilegeRepository.findByIdIn(roleUpdateRequestDto.getPrivilegeIds());
        role.setPrivileges(privileges);
        return modelMapper.map(role, RoleResponseDto.class);
    }

    @Transactional(readOnly = true)
    public Page<RoleResponseDto> search(String filter, Pageable pageable) {
        return roleRepository.findAll(RoleRepository.filterEveryWay(filter), pageable)
                .map(role -> modelMapper.map(role, RoleResponseDto.class));
    }
}
