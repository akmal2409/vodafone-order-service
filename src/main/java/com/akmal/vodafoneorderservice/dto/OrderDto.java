package com.akmal.vodafoneorderservice.dto;

import com.akmal.vodafoneorderservice.model.Order;

public record OrderDto(
    int orderId,
    String email,
    String firstName,
    String lastName,
    String productId
) {

  public static OrderDto fromOrder(Order order) {
    return new OrderDto(order.getId(), order.getEmail(), order.getFirstName(), order.getLastName(), order.getProductId());
  }
}
