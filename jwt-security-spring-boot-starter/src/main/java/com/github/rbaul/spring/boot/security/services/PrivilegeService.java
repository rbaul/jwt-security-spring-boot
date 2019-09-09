package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.domain.model.Privilege;
import com.github.rbaul.spring.boot.security.domain.repository.PrivilegeRepository;
import com.github.rbaul.spring.boot.security.web.dtos.PrivilegeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class PrivilegeService {

    private final PrivilegeRepository privilegeRepository;

    private final ModelMapper modelMapper;

    /**
     * Get All privileges
     */
    @Transactional(readOnly = true)
    public List<PrivilegeResponseDto> getAllPrivileges() {
        return privilegeRepository.findAll().stream()
                .map(role -> modelMapper.map(role, PrivilegeResponseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Remove privilege
     */
    @Transactional(readOnly = true)
    public PrivilegeResponseDto getPrivilege(long privilegeId) {
        Privilege privilege = getPrivilegeById(privilegeId);
        return modelMapper.map(privilege, PrivilegeResponseDto.class);
    }

    private Privilege getPrivilegeById(long privilegeId) {
        return privilegeRepository.findById(privilegeId)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found privilege with id: " + privilegeId, 1));
    }

    @Transactional(readOnly = true)
    public Page<PrivilegeResponseDto> search(String filter, Pageable pageable) {
        return privilegeRepository.findAll(PrivilegeRepository.filterEveryWay(filter), pageable)
                .map(privilege -> modelMapper.map(privilege, PrivilegeResponseDto.class));
    }
}
