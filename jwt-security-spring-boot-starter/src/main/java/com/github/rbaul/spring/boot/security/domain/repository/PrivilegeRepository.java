package com.github.rbaul.spring.boot.security.domain.repository;

import com.github.rbaul.spring.boot.security.domain.model.Privilege;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long>, JpaSpecificationExecutor<Privilege> {
    Optional<Privilege> findById(Long id);

    Optional<Privilege> findByName(String name);

    Collection<Privilege> findByIdIn(Collection<Long> ids);

    boolean existsByIdIn(Collection<Long> ids);

    static Specification<Privilege> filterEveryWay(String filterString) {
        if (StringUtils.isEmpty(filterString)) {
            return null;
        }
        return (Specification<Privilege>) (root, query, builder) -> {
            String pattern = "%" + filterString + "%";
            return builder
                    .or(builder.like(builder.lower(root.get(Privilege.Fields.name)), pattern),
                            builder.like(builder.lower(root.get(Privilege.Fields.description)), pattern));
        };
    }
}
