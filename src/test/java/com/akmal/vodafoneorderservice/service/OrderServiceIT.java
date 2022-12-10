package com.akmal.vodafoneorderservice.service;

import com.akmal.vodafoneorderservice.dto.OrderCreationRequest;
import com.akmal.vodafoneorderservice.dto.OrderDto;
import com.akmal.vodafoneorderservice.model.User;
import com.akmal.vodafoneorderservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.client.TestRestTemplate.HttpClientOption;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderServiceIT {

  @Container
  static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
                                                                   .withDatabaseName("integration-test-db")
                                                                   .withUsername("test")
                                                                   .withPassword("test")
                                                                   .withExposedPorts(5432);
  @LocalServerPort
  int port;

  @Autowired
  RestTemplateBuilder restTemplateBuilder;

  @Autowired
  UserRepository userRepository;

  TestRestTemplate restTemplate;

  @PostConstruct
  public void init() {
    final var delegate = this.restTemplateBuilder.rootUri(String.format("http://localhost:%d", this.port));
    this.restTemplate = new TestRestTemplate(delegate, null, null, HttpClientOption.ENABLE_COOKIES);
    this.userRepository.save(new User(1, "", "", "test@example.com"));
  }

  @DynamicPropertySource
  static void registerDbProps(DynamicPropertyRegistry propertyRegistry) {
    propertyRegistry.add("spring.datasource.url", () -> postgreSQLContainer.getJdbcUrl());
    propertyRegistry.add("spring.datasource.username", () -> postgreSQLContainer.getUsername());
    propertyRegistry.add("spring.datasource.password", () -> postgreSQLContainer.getPassword());
  }

  @Test
  @DisplayName("Should place an order if it is not a duplicate")
  void shouldPlaceOrderWhenNoPresent() {
    final var request = new OrderCreationRequest("123", "test@example.com");
    final var response = this.restTemplate.postForEntity("/api/v1/orders", request, OrderDto.class);

    assertThat(response)
        .isNotNull()
        .extracting(ResponseEntity::getStatusCode)
        .is(new Condition<>(HttpStatusCode::is2xxSuccessful, "Expected 200 OK"));
  }

  @Test
  @DisplayName("Placing duplicate order should fail")
  void shouldNotPlaceDuplicateOrder() {
    final var request = new OrderCreationRequest("123", "test@example.com");

    this.restTemplate.postForEntity("/api/v1/orders", request, OrderDto.class);

    final var duplicateResponse = this.restTemplate.postForEntity("/api/v1/orders", request, OrderDto.class);

    assertThat(duplicateResponse)
        .isNotNull()
        .extracting(ResponseEntity::getStatusCode)
        .is(new Condition<>(HttpStatusCode::isError, "Expected client error Bad Request"));
  }
}
