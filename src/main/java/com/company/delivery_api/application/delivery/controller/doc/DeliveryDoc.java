package com.company.delivery_api.application.delivery.controller.doc;

import com.company.delivery_api.application.delivery.dto.CreateDeliveryRequest;
import com.company.delivery_api.application.delivery.dto.DeliveryResponse;
import com.company.delivery_api.application.delivery.dto.UpdateDeliveryStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Deliveries", description = "Delivery management API")
public interface DeliveryDoc {

    @Operation(
            summary = "Create a new delivery",
            description = "Creates a delivery for an existing order"
    )
    ResponseEntity<DeliveryResponse> create(
            @Parameter(description = "Order ID") UUID orderId,
            CreateDeliveryRequest request
    );

    @Operation(
            summary = "Get delivery by ID",
            description = "Returns delivery details and tracking events when includeTracking=true"
    )
    ResponseEntity<?> getById(
            @Parameter(description = "Delivery ID") UUID deliveryId,
            @Parameter(description = "Include tracking events") boolean includeTracking
    );

    @Operation(
            summary = "Update delivery status",
            description = "Updates the status of an existing delivery. When status changes to IN_TRANSIT, the logged-in user is automatically assigned as the deliverer."
    )
    ResponseEntity<DeliveryResponse> updateStatus(
            @Parameter(description = "Delivery ID") UUID deliveryId,
            UpdateDeliveryStatusRequest request
    );
}

