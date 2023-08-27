package com.cass.reactive_programming;

import com.cass.reactive_programming.dto.ProductDto;
import com.cass.reactive_programming.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@WebFluxTest
@RunWith(SpringRunner.class)
class ReactiveProgrammingApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductService productService;

    @Test
    public void allProductTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("100", "Smartphone", 1, 1800));
        when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);

        webTestClient.post().uri("/products")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getProductsTest() {
        Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("17", "Coca-Cola", 1, 9.90),
                new ProductDto("200", "TV", 1, 1800));
        when(productService.getProducts()).thenReturn(productDtoFlux);

        Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(new ProductDto("17", "Coca-Cola", 1, 9.90))
                .expectNext(new ProductDto("200", "TV", 1, 1800))
                .verifyComplete();
    }

    @Test
    public void getProductByIdTest() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("17", "Coca-Cola", 1, 9.90));
        when(productService.getProductById(any())).thenReturn(productDtoMono);

        Flux<ProductDto> responseBody = webTestClient.get().uri("/products/17")
                .exchange()
                .expectStatus().isOk()
                .returnResult(ProductDto.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNextMatches(p -> p.getName().equals("Coca-Cola"))
                .verifyComplete();
    }

    @Test
    public void updateProductTest() {

        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("17", "Doce de leite", 20, 102.2));
        when(productService.updateProduct(productDtoMono, "20")).thenReturn(productDtoMono);

        webTestClient.put().uri("/products/update/20")
                .body(Mono.just(productDtoMono), ProductDto.class)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void deleteProductTest() {

        given(productService.deleteProductById(any())).willReturn(Mono.empty());

        webTestClient.delete().uri("/products/delete/20")
                .exchange()
                .expectStatus().isOk();
    }
}
