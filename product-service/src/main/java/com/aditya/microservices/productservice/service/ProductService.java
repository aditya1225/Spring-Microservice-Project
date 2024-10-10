package com.aditya.microservices.productservice.service;

import com.aditya.microservices.productservice.dto.ProductRequest;
import com.aditya.microservices.productservice.dto.ProductResponse;
import com.aditya.microservices.productservice.model.Product;
import com.aditya.microservices.productservice.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
  private final ProductRepository productRepository;

  public ProductResponse createProduct(ProductRequest productRequest) {
    Product product = Product.builder()
            .name(productRequest.name())
            .description(productRequest.description())
            .price(productRequest.price())
            .build();

    productRepository.save(product);
    log.info("Product created Successfully.");
    return new ProductResponse(product.getId(), product.getName(),
            product.getDescription(), product. getPrice());


  }

  public List<ProductResponse> getAllProducts(){
    return productRepository.findAll()
            .stream()
            .map(product -> new ProductResponse(product.getId(), product.getName(),
                    product.getDescription(), product. getPrice()))
            .toList();
  }
}
