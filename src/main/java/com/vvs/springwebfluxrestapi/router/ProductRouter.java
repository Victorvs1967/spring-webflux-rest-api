package com.vvs.springwebfluxrestapi.router;

import com.vvs.springwebfluxrestapi.handler.ProductHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductRouter {
  
  @Bean
  public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
    return RouterFunctions
      .route(GET("/products").and(accept(APPLICATION_JSON)), productHandler::getAllProducts)
      .andRoute(GET("/products/{id}").and(accept(APPLICATION_JSON)), productHandler::getProduct)
      .andRoute(POST("/products").and(accept(APPLICATION_JSON)), productHandler::saveProduct)
      .andRoute(PUT("/products/{id}").and(accept(APPLICATION_JSON)), productHandler::updateProduct)
      .andRoute(DELETE("/products/{id}").and(accept(APPLICATION_JSON)), productHandler::deleteProduct)
      .andRoute(DELETE("/products").and(accept(APPLICATION_JSON)), productHandler::deleteAllProducts)
      .andRoute(GET("/products/events").and(accept(TEXT_EVENT_STREAM)), productHandler::getProductEvents);
  }
}
