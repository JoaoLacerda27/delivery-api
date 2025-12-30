package com.company.delivery_api.application.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Request to create a new order")
public record CreateOrderRequest(
        @NotBlank(message = "Customer name is mandatory")
        @Schema(description = "Customer full name", example = "John Doe")
        String customerName,

        @NotNull(message = "Total amount is mandatory")
        @Positive(message = "Total amount must be positive")
        @Schema(description = "Order total amount", example = "99.99")
        BigDecimal totalAmount
) {}

