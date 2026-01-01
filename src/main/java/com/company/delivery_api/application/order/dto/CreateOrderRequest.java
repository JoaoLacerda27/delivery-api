package com.company.delivery_api.application.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Request to create a new order")
public record CreateOrderRequest(
        @Schema(description = "Customer ID (optional)", example = "6348")
        String customerId,
        
        @NotBlank(message = "Customer name is mandatory")
        @Schema(description = "Customer full name", example = "John Doe")
        String customerName,

        @Schema(description = "Order total amount (calculated automatically if items are provided)", example = "99.99")
        BigDecimal totalAmount,
        
        @Valid
        @Schema(description = "Order items (optional, if provided totalAmount will be calculated automatically)")
        List<OrderItemRequest> items
) {
    public BigDecimal getCalculatedTotalAmount() {
        if (totalAmount != null) {
            return totalAmount;
        }
        if (items != null && !items.isEmpty()) {
            return items.stream()
                    .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return null;
    }
}

