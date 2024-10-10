package com.aditya.microservices.productservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.shaded.org.hamcrest.Matchers;

import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

  @ServiceConnection
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");

  @LocalServerPort
  private Integer port;


  @BeforeEach
  public void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  static{
    mongoDBContainer.start();
  }
  @Test
  void shouldCreateProduct() {
    String requestBody = """
            {
              "name": "Iphone",
              "description": "Latest Iphone release",
              "price": 10000.0
            }
            """;

    RestAssured.given()
            .contentType("application/json")
            .body(requestBody)
            .when()
            .post("/api/product")
            .then()
            .statusCode(201)
            .body("id", org.hamcrest.Matchers.notNullValue())
            .body("name", org.hamcrest.Matchers.equalTo("Iphone"))
            .body("description", org.hamcrest.Matchers.equalTo("Latest Iphone release"))
            .body("price", org.hamcrest.Matchers.equalTo(10000.0F));


  }

}
