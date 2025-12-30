package com.company.delivery_api.application.delivery.controller;

import com.company.delivery_api.application.delivery.controller.doc.DeliveryDoc;
import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import com.company.delivery_api.application.delivery.dto.CreateDeliveryRequest;
import com.company.delivery_api.application.delivery.dto.DeliveryResponse;
import com.company.delivery_api.application.delivery.dto.DeliveryWithTrackingResponse;
import com.company.delivery_api.application.delivery.dto.UpdateDeliveryStatusRequest;
import com.company.delivery_api.application.delivery.service.DeliveryQueryService;
import com.company.delivery_api.application.delivery.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryDoc {

    private final DeliveryService deliveryService;
    private final DeliveryQueryService deliveryQueryService;

    @PostMapping("/{orderId}")
    @Override
    public ResponseEntity<DeliveryResponse> create(
            @PathVariable UUID orderId,
            @RequestBody @Valid CreateDeliveryRequest request
    ) {
        Delivery delivery = deliveryService.createDelivery(
                orderId,
                request.street(),
                request.city(),
                request.state(),
                request.zipCode()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(delivery));
    }

    @GetMapping("/{deliveryId}")
    @Override
    public ResponseEntity<?> getById(
            @PathVariable UUID deliveryId,
            @RequestParam(defaultValue = "false") boolean includeTracking
    ) {
        Delivery delivery = deliveryQueryService.findById(deliveryId);
        
        if (!includeTracking) {
            return ResponseEntity.ok(toResponse(delivery));
        }

        var events = deliveryQueryService.findEvents(deliveryId);
        return ResponseEntity.ok(new DeliveryWithTrackingResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getStreet(),
                delivery.getCity(),
                delivery.getState(),
                delivery.getZipCode(),
                delivery.getStatus().name(),
                delivery.getCreatedAt(),
                events
        ));
    }

    @PatchMapping("/{deliveryId}/status")
    @Override
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

