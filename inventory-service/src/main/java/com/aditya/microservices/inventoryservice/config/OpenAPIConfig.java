package com.aditya.microservices.inventoryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {
  @Bean
  public OpenAPI productServiceAPI() {
    return new OpenAPI()
            .info(new Info().title("Inventory Service API")
                    .description("This is the rest api for inventory service")
                    .version("v0.0.1")
                    .license(new License().name("Apache 2.0")))
            .externalDocs(new ExternalDocumentation()
                    .description("You can refer to the inventory service wiki documentation")
                    .url("http://inventory-service.com/docs")
            );
  }
}