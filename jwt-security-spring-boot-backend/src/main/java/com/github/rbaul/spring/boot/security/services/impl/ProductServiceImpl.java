package com.github.rbaul.spring.boot.security.services.impl;

import com.github.rbaul.spring.boot.security.domain.model.Product;
import com.github.rbaul.spring.boot.security.domain.repository.ProductRepository;
import com.github.rbaul.spring.boot.security.services.ProductService;
import com.github.rbaul.spring.boot.security.web.dtos.ProductDto;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public ProductDto create(ProductDto productDto) {
        Product product = productRepository.save(modelMapper.map(productDto, Product.class));
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    @Override
    public ProductDto update(Long productId, ProductDto productDto) {
        Product product = getProductById(productId);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setState(productDto.getState());
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDto get(Long productId) {
        Product product = getProductById(productId);
        return modelMapper.map(product, ProductDto.class);
    }

    @Transactional
    @Override
    public void delete(Long productId) {
        if (!productRepository.existsById(productId)){
            throw new EmptyResultDataAccessException("No found product with id: " + productId, 1);
        }
        productRepository.deleteById(productId);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found product with id: " + productId, 1));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDto> getAll() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDto> getPageable(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> modelMapper.map(product, ProductDto.class));
    }
}
