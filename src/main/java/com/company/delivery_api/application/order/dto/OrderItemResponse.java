package com.company.delivery_api.application.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Order item information")
public record OrderItemResponse(
        @Schema(description = "Order item unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        
        @Schema(description = "Product name", example = "Carpete")
        String productName,
        
        @Schema(description = "Item quantity", example = "6")
        Integer quantity,
        
        @Schema(description = "Item unit price", example = "10.90")
        BigDecimal price
) {}

