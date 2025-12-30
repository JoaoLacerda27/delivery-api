package com.company.delivery_api.application.delivery.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record DeliveryWithTrackingResponse(
        UUID id,
        UUID orderId,
        String street,
        String city,
        String state,
        String zipCode,
        String status,
        Instant createdAt,
        List<TrackingEventResponse> events
) {}

