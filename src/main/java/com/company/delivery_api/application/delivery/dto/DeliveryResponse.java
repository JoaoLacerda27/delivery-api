package com.company.delivery_api.application.delivery.dto;

import com.company.delivery_api.application.delivery.domain.postgres.enums.DeliveryStatusEnum;

import java.time.Instant;
import java.util.UUID;

public record DeliveryResponse(
        UUID id,
        UUID orderId,
        String street,
        String city,
        String state,
        String zipCode,
        DeliveryStatusEnum status,
        Instant createdAt
) {}

