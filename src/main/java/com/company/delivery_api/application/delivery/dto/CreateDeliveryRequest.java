package com.company.delivery_api.application.delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDeliveryRequest(
        @NotNull(message = "Order ID is mandatory")
        UUID orderId,

        @NotBlank(message = "Street is mandatory")
        String street,

        @NotBlank(message = "City is mandatory")
        String city,

        @NotBlank(message = "State is mandatory")
        String state,

        @NotBlank(message = "Zip code is mandatory")
        String zipCode
) {}

