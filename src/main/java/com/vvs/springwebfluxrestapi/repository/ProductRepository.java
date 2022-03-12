package com.vvs.springwebfluxrestapi.repository;

import com.vvs.springwebfluxrestapi.model.Product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
  
}
