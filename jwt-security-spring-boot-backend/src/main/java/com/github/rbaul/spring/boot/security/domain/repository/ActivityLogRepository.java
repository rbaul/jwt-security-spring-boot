package com.github.rbaul.spring.boot.security.domain.repository;

import com.github.rbaul.spring.boot.security.domain.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
}
