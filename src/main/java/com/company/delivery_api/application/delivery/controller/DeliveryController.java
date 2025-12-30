package com.company.delivery_api.application.delivery.controller;

import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import com.company.delivery_api.application.delivery.dto.CreateDeliveryRequest;
import com.company.delivery_api.application.delivery.dto.DeliveryResponse;
import com.company.delivery_api.application.delivery.dto.UpdateDeliveryStatusRequest;
import com.company.delivery_api.application.delivery.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@Tag(name = "Deliveries", description = "Delivery management API")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    @Operation(summary = "Create a new delivery", description = "Creates a delivery for an existing order")
    public ResponseEntity<DeliveryResponse> create(
            @RequestBody @Valid CreateDeliveryRequest request
    ) {
        Delivery delivery = deliveryService.createDelivery(
                request.orderId(),
                request.street(),
                request.city(),
                request.state(),
                request.zipCode()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(delivery));
    }

    @PatchMapping("/{deliveryId}/status")
    @Operation(summary = "Update delivery status", description = "Updates the status of an existing delivery")
    public ResponseEntity<DeliveryResponse> updateStatus(
            @PathVariable UUID deliveryId,
            @RequestBody @Valid UpdateDeliveryStatusRequest request
    ) {
        Delivery delivery = deliveryService.updateStatus(
                deliveryId,
                request.status()
        );

        return ResponseEntity.ok(toResponse(delivery));
    }

    private DeliveryResponse toResponse(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getStreet(),
                delivery.getCity(),
                delivery.getState(),
                delivery.getZipCode(),
                delivery.getStatus(),
                delivery.getCreatedAt()
        );
    }
}

