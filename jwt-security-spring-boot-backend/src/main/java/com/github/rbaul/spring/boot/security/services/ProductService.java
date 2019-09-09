package com.github.rbaul.spring.boot.security.services;

import com.github.rbaul.spring.boot.security.web.dtos.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto productDto);

    ProductDto update(Long productId, ProductDto productDto);

    ProductDto get(Long productId);

    void delete(Long productId);

    List<ProductDto> getAll();

    Page<ProductDto> search(String filter, Pageable pageable);
}
