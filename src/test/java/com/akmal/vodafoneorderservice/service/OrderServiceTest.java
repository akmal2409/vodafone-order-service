package com.akmal.vodafoneorderservice.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.akmal.vodafoneorderservice.dto.OrderCreationRequest;
import com.akmal.vodafoneorderservice.exception.OrderPlacementException;
import com.akmal.vodafoneorderservice.model.Order;
import com.akmal.vodafoneorderservice.model.User;
import com.akmal.vodafoneorderservice.repository.OrderRepository;
import com.akmal.vodafoneorderservice.repository.UserRepository;
import java.util.Optional;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * The tests do not cover validity of the input arguments since we delegate that to the spring-validation library.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  OrderRepository orderRepository;

  @Mock
  UserRepository userRepository;

  @InjectMocks
  OrderService orderService;

  @Test
  @DisplayName("Test placeOrder() should throw an exception when provided user does not exist in the database")
  void shouldThrowExceptionWhenUserDoesntExist() {
    // given
    final var orderRequest = new OrderCreationRequest("1", "nonExistent@example.com");

    // when
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> {
      this.orderService.placeOrder(orderRequest);
    }).isInstanceOf(OrderPlacementException.class);

    verify(userRepository, times(1)).findByEmail(anyString());
    verifyNoInteractions(orderRepository);
  }

  @Test
  @DisplayName("Test placeOrder() should throw an exception when duplicate order is placed")
  void shouldThrowExceptionWhenDuplicateOrder() {
    // given
    final var orderRequest = new OrderCreationRequest("1", "existing@example.com");

    // when
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User(1, null, null, orderRequest.email())));
    when(orderRepository.save(any(Order.class))).thenThrow(new DataIntegrityViolationException("Duplicate key for product email pair",
        new ConstraintViolationException("duplicate key", null, "unique")));

    // then
    assertThatThrownBy(() -> {
      this.orderService.placeOrder(orderRequest);
    }).isInstanceOf(OrderPlacementException.class)
        .extracting(Throwable::getCause)
        .isInstanceOf(DataIntegrityViolationException.class);

    verify(userRepository, times(1)).findByEmail(anyString());
    verify(orderRepository, times(1)).save(any(Order.class));
  }
}
