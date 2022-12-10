package com.akmal.vodafoneorderservice.controller;

import com.akmal.vodafoneorderservice.dto.OrderCreationRequest;
import com.akmal.vodafoneorderservice.dto.OrderDto;
import com.akmal.vodafoneorderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(OrderController.BASE_URL)
@Tag(name = "Orders API", description = "Interface for manipulating orders")
@RequiredArgsConstructor
@RestController
public class OrderController {

  protected static final String BASE_URL = "/api/v1/orders";

  private final OrderService orderService;

  @Operation(summary = "Finds all orders")
  @ApiResponse(responseCode = "200", description = "Found all", content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
      array = @ArraySchema(schema = @Schema(implementation = OrderDto.class)))})
  @GetMapping
  public Collection<OrderDto> findAll() {
    return orderService.findAll();
  }

  @Operation(summary = "Places an order")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Placed an order successfully", content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = OrderDto.class))}),
      @ApiResponse(responseCode = "400", description = "Failed to place an order non existent user/duplicate order")
  })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderDto placeOrder(@RequestBody @Valid OrderCreationRequest creationRequest) {
    return orderService.placeOrder(creationRequest);
  }
}
