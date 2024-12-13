package com.aditya.microservices.orderservice;

import com.aditya.microservices.orderservice.stubs.InventoryClientStub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import io.restassured.RestAssured;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;

//@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

  @ServiceConnection
  static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
  @LocalServerPort
  private Integer port;

  @BeforeEach
  void setup() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  static {
    mySQLContainer.start();
  }

  @Test
  void shouldSubmitOrder() {
    String submitOrderJson = """
                {
                     "skuCode": "iphone_15",
                     "price": 1000,
                     "quantity": 1
                }
                """;
    InventoryClientStub.stubInventoryCall("iphone_15", 1);

    var responseBodyString = RestAssured.given()
            .contentType("application/json")
            .body(submitOrderJson)
            .when()
            .post("/api/order")
            .then()
            .log().all()
            .statusCode(201)
            .extract()
            .body().asString();

    assertThat(responseBodyString, Matchers.is("Order Placed"));
  }

  @Test
  void shouldFailOrderWhenProductIsNotInStock() {
    String submitOrderJson = """
                {
                     "skuCode": "iphone_15",
                     "price": 1000,
                     "quantity": 1000
                }
                """;
    InventoryClientStub.stubInventoryCall("iphone_15", 1000);

    RestAssured.given()
            .contentType("application/json")
            .body(submitOrderJson)
            .when()
            .post("/api/order")
            .then()
            .log().all()
            .statusCode(500);
  }
}