package com.github.rbaul.spring.boot.security.domain.repository;

import com.github.rbaul.spring.boot.security.domain.model.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    static Specification<Product> filterEveryWay(String filterString) {
        if (StringUtils.isEmpty(filterString)) {
            return null;
        }
        return (Specification<Product>) (root, query, builder) -> {
            String pattern = "%" + filterString + "%";
            return builder
                    .or(builder.like(builder.lower(root.get(Product.Fields.name)), pattern),
                            builder.like(builder.lower(root.get(Product.Fields.description)), pattern));
        };
    }
}
