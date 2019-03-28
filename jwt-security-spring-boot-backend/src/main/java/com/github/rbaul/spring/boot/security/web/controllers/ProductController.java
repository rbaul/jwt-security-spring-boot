package com.github.rbaul.spring.boot.security.web.controllers;

import com.github.rbaul.spring.boot.security.config.ApiImplicitPageable;
import com.github.rbaul.spring.boot.security.services.ProductService;
import com.github.rbaul.spring.boot.security.web.ValidationGroups;
import com.github.rbaul.spring.boot.security.web.dtos.ProductDto;
import com.github.rbaul.spring.boot.security.web.dtos.errors.ErrorDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value = "Get Product")
    @ApiResponses({@ApiResponse(code = 200, message = "Retrieved Product"),
            @ApiResponse(code = 404, message = "Product Not Found", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("{productId}")
    public ProductDto getProduct(@PathVariable long productId) {
        return productService.get(productId);
    }

    @ApiOperation(value = "Get All Product")
    @ApiResponses({@ApiResponse(code = 200, message = "Retrieved All Product")})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping("all")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public List<ProductDto> getAllProduct() {
        return productService.getAll();
    }


    @ApiOperation(value = "Create Product")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully Product created"),
            @ApiResponse(code = 428, message = "Invalid Product info", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ProductDto create(
            @ApiParam(value = "Product object that needs to be create", name = "ProductDto", required = true)
            @Validated(ValidationGroups.Create.class) @RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }

    @ApiOperation(value = "Update product")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully Product updated"),
            @ApiResponse(code = 428, message = "Invalid product info", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @PutMapping("{productId}")
    public ProductDto update(@PathVariable long productId,
                                   @ApiParam(value = "Product object that needs to be edit", name = "ProductDto", required = true)
                                   @Validated(ValidationGroups.Create.class) @RequestBody ProductDto productDto) {
        return productService.update(productId, productDto);
    }

    @ApiOperation(value = "Delete Product")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Successfully Product deleted"),
            @ApiResponse(code = 428, message = "Invalid product Id", response = ErrorDto.class)})
    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping("{productId}")
    public void delete(@PathVariable long productId) {
        productService.delete(productId);
    }

    @ApiOperation(value = "Fetch all products with paging")
    @ApiImplicitPageable
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully lists products")})
    @ResponseStatus(code = HttpStatus.OK)
    @GetMapping
    public Page<ProductDto> fetch(@PageableDefault @ApiIgnore(
            "Ignored because swagger ui shows the wrong params, " +
                    "instead they are explained in the implicit params"
    ) Pageable pageable) {
        return productService.getPageable(pageable);
    }

}
