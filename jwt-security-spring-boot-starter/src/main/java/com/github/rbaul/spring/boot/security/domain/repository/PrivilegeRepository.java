package com.github.rbaul.spring.boot.security.domain.repository;

import com.github.rbaul.spring.boot.security.domain.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Optional<Privilege> findById(Long id);

    Optional<Privilege> findByName(String name);

    Collection<Privilege> findByIdIn(Collection<Long> ids);

    boolean existsByIdIn(Collection<Long> ids);
}
