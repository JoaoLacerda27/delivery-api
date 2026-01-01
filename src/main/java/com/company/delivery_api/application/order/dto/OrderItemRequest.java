package com.company.delivery_api.application.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Order item information")
public record OrderItemRequest(
        @NotBlank(message = "Product name is mandatory")
        @Schema(description = "Product name", example = "Carpete")
        String productName,
        
        @NotNull(message = "Quantity is mandatory")
        @Positive(message = "Quantity must be positive")
        @Schema(description = "Item quantity", example = "6")
        Integer quantity,
        
        @NotNull(message = "Price is mandatory")
        @Positive(message = "Price must be positive")
        @Schema(description = "Item unit price", example = "10.90")
        BigDecimal price
) {}


