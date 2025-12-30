package com.company.delivery_api.application.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customerName,
        BigDecimal totalAmount,
        Instant createdAt
) {}

