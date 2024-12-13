package com.aditya.microservices.orderservice.repository;

import com.aditya.microservices.orderservice.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
