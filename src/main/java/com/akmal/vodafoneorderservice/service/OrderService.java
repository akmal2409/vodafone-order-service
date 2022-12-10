package com.akmal.vodafoneorderservice.service;

import static com.akmal.vodafoneorderservice.model.Order.UNIQUE_ORDER_CONSTRAINT;

import com.akmal.vodafoneorderservice.dto.OrderCreationRequest;
import com.akmal.vodafoneorderservice.dto.OrderDto;
import com.akmal.vodafoneorderservice.exception.OrderPlacementException;
import com.akmal.vodafoneorderservice.model.Order;
import com.akmal.vodafoneorderservice.repository.OrderRepository;
import com.akmal.vodafoneorderservice.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;

  /**
   * The method persists the order to the database.
   * Invariant is that the creationRequest is validated by the spring-validation processor.
   * The method ensures that no two same orders for a product from the same user exist by leveraging
   * the schema definition, in our case we have to set the tuple (email, product_id) under the UNIQUE constraint.
   * Then, if the constraint is violated spring will translate underlying exception to the {@link DataIntegrityViolationException}
   * with a cause of {@link ConstraintViolationException} which we can catch and return a more informative response to a client.
   * @param creationRequest
   * @return
   */
  @Transactional
  public OrderDto placeOrder(@Valid OrderCreationRequest creationRequest) {
    final var user = userRepository.findByEmail(creationRequest.email())
                         .orElseThrow(() -> new OrderPlacementException("User with provided email was not found"));

    final var order = new Order(null, user.email(), user.firstName(), user.lastName(),
        creationRequest.productId());
    Order savedOrder = null;

    try {
      savedOrder = this.orderRepository.save(order);
    } catch (DataIntegrityViolationException ex) {
      for (Throwable cause = ex.getCause(); cause != null; cause = cause.getCause()) {
        if (cause instanceof ConstraintViolationException constraintViolationException &&
                UNIQUE_ORDER_CONSTRAINT.equals(constraintViolationException.getConstraintName())) {
          throw new OrderPlacementException("Failed to place an order. Reason: the order already exists", ex);
        }
      }

      // otherwise rethrow
      throw ex;
    }

    return OrderDto.fromOrder(savedOrder);
  }


  @Transactional(readOnly = true)
  public Collection<OrderDto> findAll() {
    return this.orderRepository
               .findAll()
               .stream().map(OrderDto::fromOrder)
               .toList();
  }
}
