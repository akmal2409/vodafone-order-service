package com.akmal.vodafoneorderservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrderCreationRequest(
    @NotBlank String productId,
    @NotBlank @Email String email
) {


}
