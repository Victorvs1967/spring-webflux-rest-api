package com.vvs.springwebfluxrestapi.handler;

import com.vvs.springwebfluxrestapi.model.Product;
import com.vvs.springwebfluxrestapi.model.ProductEvent;
import com.vvs.springwebfluxrestapi.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.HttpStatus.CREATED;

@Component
public class ProductHandler {
  
  @Autowired
  private ProductRepository productRepository;

  public Mono<ServerResponse> getAllProducts(ServerRequest request) {
    Flux<Product> products = productRepository.findAll();

    return ServerResponse
      .ok()
      .contentType(APPLICATION_JSON)
      .body(products, Product.class);
  }

  public Mono<ServerResponse> getProduct(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<Product> product = productRepository.findById(id);

    return product.flatMap(prod -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(BodyInserters.fromValue(prod)))
      .switchIfEmpty(ServerResponse.badRequest().build());
  }

  public Mono<ServerResponse> saveProduct(ServerRequest request) {
    Mono<Product> product = request.bodyToMono(Product.class);

    return product.flatMap(prod -> ServerResponse
      .status(CREATED)
      .contentType(APPLICATION_JSON)
      .body(productRepository.save(prod), Product.class));
  }

  public Mono<ServerResponse> updateProduct(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<Product> existingProduct = productRepository.findById(id);
    Mono<Product> product = request.bodyToMono(Product.class);

    return product.zipWith(existingProduct, (prod, existingProd) -> new Product(existingProd.getId(), prod.getName(), prod.getPrice()))
      .flatMap(prod -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(productRepository.save(prod), Product.class))
      .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteProduct(ServerRequest request) {
    String id = request.pathVariable("id");
    Mono<Product> product = productRepository.findById(id);

    return product.flatMap(prod -> ServerResponse
        .ok()
        .build(productRepository.delete(prod)))
      .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteAllProducts(ServerRequest request) {

    return ServerResponse
      .ok()
      .contentType(APPLICATION_JSON)
      .body(productRepository.deleteAll(), Product.class);
  }

  public Mono<ServerResponse> getProductEvents(ServerRequest request) {
    Flux<ProductEvent> events = Flux.interval(Duration.ofSeconds(1))
      .map(val -> new ProductEvent(val, "Product Event"));

    return ServerResponse
      .ok()
      .contentType(TEXT_EVENT_STREAM)
      .body(events, ProductEvent.class);
  }
}
