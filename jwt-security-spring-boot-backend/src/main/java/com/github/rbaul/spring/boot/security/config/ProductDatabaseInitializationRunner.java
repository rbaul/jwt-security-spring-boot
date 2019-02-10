package com.github.rbaul.spring.boot.security.config;

import com.github.rbaul.spring.boot.security.domain.model.Product;
import com.github.rbaul.spring.boot.security.domain.model.types.ProductState;
import com.github.rbaul.spring.boot.security.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Component
public class ProductDatabaseInitializationRunner implements ApplicationRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(ApplicationArguments args) {
        Product product1 = Product.builder()
                .name("Shirt")
                .description("Woman shirt")
                .price(49.99)
                .state(ProductState.REGULAR).build();
        Product product2 = Product.builder()
                .name("Shirt")
                .description("Man shirt")
                .price(0.0)
                .state(ProductState.SOLD_OUT).build();
        Product product3 = Product.builder()
                .name("Desktop PC")
                .description("Home PC")
                .price(199.99)
                .state(ProductState.SALE).build();
        Product product4 = Product.builder()
                .name("Laptop")
                .description("Best laptop i9")
                .price(549.99)
                .state(ProductState.COMING_SOON).build();

        List<Product> products = productRepository.saveAll(Arrays.asList(product1, product2, product3, product4));
        log.info("Created database content with: {}", products);
    }
}
