package com.github.rbaul.spring.boot.security.domain.repository;

import com.github.rbaul.spring.boot.security.domain.model.Role;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    Collection<Role> findByIdIn(Collection<Long> ids);

    boolean existsByName(String name);

    static Specification<Role> filterEveryWay(String filterString) {
        if (StringUtils.isEmpty(filterString)) {
            return null;
        }
        return (Specification<Role>) (root, query, builder) -> {
            String pattern = "%" + filterString + "%";
            return builder
                    .or(builder.like(builder.lower(root.get(Role.Fields.name)), pattern),
                            builder.like(builder.lower(root.get(Role.Fields.description)), pattern));
        };
    }
}
