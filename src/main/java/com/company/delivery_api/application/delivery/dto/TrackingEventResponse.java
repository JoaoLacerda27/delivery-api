package com.company.delivery_api.application.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Tracking event information")
public record TrackingEventResponse(
        @Schema(description = "Event description", example = "Package picked up from warehouse")
        String description,
        
        @Schema(description = "Event source", example = "SYSTEM")
        String source,
        
        @Schema(description = "Event occurrence timestamp", example = "2024-01-15T10:30:00Z")
        Instant occurredAt
) {}

