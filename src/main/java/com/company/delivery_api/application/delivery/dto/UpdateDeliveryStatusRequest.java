package com.company.delivery_api.application.delivery.dto;

import com.company.delivery_api.application.delivery.domain.postgres.enums.DeliveryStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to update delivery status")
public record UpdateDeliveryStatusRequest(
        @NotNull(message = "Status is mandatory")
        @Schema(description = "New delivery status", example = "IN_TRANSIT", 
                allowableValues = {"PENDING", "IN_TRANSIT", "DELIVERED", "FAILED"})
        DeliveryStatusEnum status
) {}

