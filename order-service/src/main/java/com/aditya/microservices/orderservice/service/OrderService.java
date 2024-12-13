package com.aditya.microservices.orderservice.service;

import com.aditya.microservices.orderservice.dto.OrderRequest;
import com.aditya.microservices.orderservice.event.OrderPlacedEvent;
import com.aditya.microservices.orderservice.inventory.InventoryClient;
import com.aditya.microservices.orderservice.model.Order;
import com.aditya.microservices.orderservice.repository.OrderRepository;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
  private final OrderRepository orderRepository;
  private final InventoryClient inventoryClient;
  private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

  public void placeOrder(OrderRequest orderRequest) {
    String sku = orderRequest.skuCode();
    Integer quantity = orderRequest.quantity();
    var isProductinStock = inventoryClient.inStock(sku, quantity);

    if (isProductinStock) {
      Order order = new Order();
      order.setOrderNumber(UUID.randomUUID().toString());
      order.setPrice(orderRequest.price());
      order.setSkuCode(orderRequest.skuCode());
      order.setQuantity(orderRequest.quantity());
      orderRepository.save(order);

      OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
      orderPlacedEvent.setOrderNumber(order.getOrderNumber());
      orderPlacedEvent.setEmail(orderRequest.userDetails().email());
      log.info("Start - Sending OrderPlacedEvent {} to kafka topic order-placed", orderPlacedEvent);
      kafkaTemplate.send("order-placed", orderPlacedEvent);
      log.info("End - Sending OrderPlacedEvent {} to kafka topic order-placed", orderPlacedEvent);

    }else{
      throw new RuntimeException("Product with SkuCode" + orderRequest.skuCode() + " is not in stock");
    }
  }
}
