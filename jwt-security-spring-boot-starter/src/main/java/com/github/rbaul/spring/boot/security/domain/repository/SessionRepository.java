package com.github.rbaul.spring.boot.security.domain.repository;

import com.github.rbaul.spring.boot.security.domain.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findByUsername(String username);

    Optional<Session> findById(String id);

    Optional<Session> findByIdAndUsername(String username, String id);

    boolean existsByUsername(String userName);
}