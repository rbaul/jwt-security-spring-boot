package com.github.rbaul.spring.boot.security.domain.repository;

import com.github.rbaul.spring.boot.security.domain.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String userName);

    boolean existsByUsername(String userName);

    long countByRoles_privileges_name(String privilegeName);

    static Specification<User> filterEveryWay(String filterString) {
        if (StringUtils.isEmpty(filterString)) {
            return null;
        }
        return (Specification<User>) (root, query, builder) -> {
            String pattern = "%" + filterString + "%";
            return builder
                    .or(builder.like(builder.lower(root.get(User.Fields.firstName)), pattern),
                            builder.like(builder.lower(root.get(User.Fields.lastName)), pattern),
                            builder.like(builder.lower(root.get(User.Fields.username)), pattern));
        };
    }
}