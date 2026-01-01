package com.company.delivery_api.application.order.dto;

import com.company.delivery_api.application.order.domain.postgres.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Schema(description = "Order information")
public record OrderResponse(
        @Schema(description = "Order unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        
        @Schema(description = "Customer ID", example = "6348")
        String customerId,
        
        @Schema(description = "Customer full name", example = "John Doe")
        String customerName,
        
        @Schema(description = "Order total amount", example = "99.99")
        BigDecimal totalAmount,
        
        @Schema(description = "Order status", example = "CREATED", allowableValues = {"CREATED", "PAID", "SHIPPED", "DELIVERED", "CANCELED"})
        OrderStatusEnum status,
        
        @Schema(description = "Order items", example = "[]")
        java.util.List<OrderItemResponse> items,
        
        @Schema(description = "Order creation timestamp", example = "2024-01-15T10:30:00Z")
        Instant createdAt
) {}

