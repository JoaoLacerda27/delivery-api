package com.company.delivery_api.application.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotBlank(message = "Customer name is mandatory")
        String customerName,

        @NotNull(message = "Total amount is mandatory")
        @Positive(message = "Total amount must be positive")
        BigDecimal totalAmount
) {}

