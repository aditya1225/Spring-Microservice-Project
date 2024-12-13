package com.aditya.microservices.orderservice.inventory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;


public interface InventoryClient {

  @GetExchange("/api/inventory")
  boolean inStock(@RequestParam String skuCode,@RequestParam Integer quantity);


}
