package com.akmal.vodafoneorderservice.controller;

import com.akmal.vodafoneorderservice.dto.OrderCreationRequest;
import com.akmal.vodafoneorderservice.dto.OrderDto;
import com.akmal.vodafoneorderservice.service.OrderService;
import jakarta.validation.Valid;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(OrderController.BASE_URL)
@RequiredArgsConstructor
@RestController
public class OrderController {

  protected static final String BASE_URL = "/api/v1/orders";

  private final OrderService orderService;

  @GetMapping
  public Collection<OrderDto> findAll() {
    return orderService.findAll();
  }

  @PostMapping
  public OrderDto placeOrder(@RequestBody @Valid OrderCreationRequest creationRequest) {
    return orderService.placeOrder(creationRequest);
  }
}
