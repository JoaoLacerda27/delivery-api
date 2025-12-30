package com.company.delivery_api.application.delivery.dto;

import java.time.Instant;

public record TrackingEventResponse(
        String description,
        String source,
        Instant occurredAt
) {}

