package com.company.delivery_api.application.delivery.dto;

import com.company.delivery_api.application.delivery.domain.postgres.enums.DeliveryStatusEnum;
import jakarta.validation.constraints.NotNull;

public record UpdateDeliveryStatusRequest(
        @NotNull(message = "Status is mandatory")
        DeliveryStatusEnum status
) {}

