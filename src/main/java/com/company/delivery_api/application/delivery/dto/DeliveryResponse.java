package com.company.delivery_api.application.delivery.dto;

import com.company.delivery_api.application.delivery.domain.mongo.AddressInfo;
import com.company.delivery_api.application.delivery.domain.postgres.enums.DeliveryStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "Delivery information")
public record DeliveryResponse(
        @Schema(description = "Delivery unique identifier", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        
        @Schema(description = "Associated order ID", example = "123e4567-e89b-12d3-a456-426614174001")
        UUID orderId,
        
        @Schema(description = "Street address", example = "Main Street")
        String street,
        
        @Schema(description = "City name", example = "São Paulo")
        String city,
        
        @Schema(description = "State abbreviation", example = "SP")
        String state,
        
        @Schema(description = "ZIP/CEP code", example = "01001000")
        String zipCode,
        
        @Schema(description = "Current delivery status", example = "IN_TRANSIT")
        DeliveryStatusEnum status,
        
        @Schema(description = "Delivery creation timestamp", example = "2024-01-15T10:30:00Z")
        Instant createdAt,
        
        @Schema(description = "Name of the deliverer (assigned when status changes to IN_TRANSIT)", example = "João Silva")
        String delivererName,
        
        @Schema(description = "Address information from ViaCEP (if available)")
        AddressInfo addressInfo
) {}

