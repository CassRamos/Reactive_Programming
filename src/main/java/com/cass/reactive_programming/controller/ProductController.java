package com.cass.reactive_programming.controller;

import com.cass.reactive_programming.dto.ProductDto;
import com.cass.reactive_programming.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //Flux -> emits 0 or more elements
    //Mono -> emits at most one element

    @GetMapping
    public Flux<ProductDto> getAllProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Mono<ProductDto> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/product-range")
    public Flux<ProductDto> getProductBetweenPriceRange(@RequestParam("min") double min,
                                                   @RequestParam("max") double max) {
        return productService.getProductByPriceRange(min, max);
    }

    @PostMapping
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        return productService.saveProduct(productDtoMono);
    }

    @PutMapping("/update/{id}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono,
                                          @PathVariable String id) {
        return productService.updateProduct(productDtoMono, id);
    }

    @DeleteMapping("delete/{id}")
    public Mono<Void> deleteProduct(@PathVariable String id) {
        return productService.deleteProductById(id);
    }

}
