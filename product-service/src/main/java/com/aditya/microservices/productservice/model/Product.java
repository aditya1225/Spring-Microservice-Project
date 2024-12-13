package com.aditya.microservices.productservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

import javax.imageio.ImageIO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = lombok.AccessLevel.PUBLIC)
@Data
public class Product {
  @Id
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
  private byte[] image;

}
