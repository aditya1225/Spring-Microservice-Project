package com.aditya.microservices.productservice.repository;

import com.aditya.microservices.productservice.model.Product;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
