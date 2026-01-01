package com.company.delivery_api.application.delivery.controller;

import com.company.delivery_api.application.delivery.controller.doc.DeliveryDoc;
import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import com.company.delivery_api.application.delivery.dto.CreateDeliveryRequest;
import com.company.delivery_api.application.delivery.dto.DeliveryResponse;
import com.company.delivery_api.application.delivery.dto.DeliveryWithTrackingResponse;
import com.company.delivery_api.application.delivery.dto.UpdateDeliveryStatusRequest;
import com.company.delivery_api.application.delivery.service.DeliveryQueryService;
import com.company.delivery_api.application.delivery.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController implements DeliveryDoc {

    private final DeliveryService deliveryService;
    private final DeliveryQueryService deliveryQueryService;
    private final com.company.delivery_api.application.delivery.repository.postgres.DeliveryRepository deliveryRepository;

    @GetMapping
    @Operation(summary = "List all deliveries", description = "Returns a paginated list of all deliveries with address information")
    public ResponseEntity<Page<DeliveryResponse>> getAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<Delivery> deliveries = deliveryRepository.findAll(pageable);
        Page<DeliveryResponse> response = deliveries.map(delivery -> {
            var addressInfo = deliveryQueryService.getOrFetchAddress(delivery.getId(), delivery.getZipCode());
            return toResponse(delivery, addressInfo);
        });
        return ResponseEntity.ok(response);
    }

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
        var addressInfo = deliveryQueryService.getOrFetchAddress(deliveryId, delivery.getZipCode());
        
        if (!includeTracking) {
            return ResponseEntity.ok(toResponse(delivery, addressInfo));
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
                events,
                addressInfo
        ));
    }

    @PatchMapping("/{deliveryId}/status")
    @Override
    public ResponseEntity<DeliveryResponse> updateStatus(
            @PathVariable UUID deliveryId,
            @RequestBody @Valid UpdateDeliveryStatusRequest request
    ) {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        Delivery delivery = deliveryService.updateStatus(
                deliveryId,
                request.status(),
                authentication
        );

        var addressInfo = deliveryQueryService.getOrFetchAddress(delivery.getId(), delivery.getZipCode());
        return ResponseEntity.ok(toResponse(delivery, addressInfo));
    }

    private DeliveryResponse toResponse(Delivery delivery) {
        return toResponse(delivery, null);
    }

    private DeliveryResponse toResponse(Delivery delivery, com.company.delivery_api.application.delivery.domain.mongo.AddressInfo addressInfo) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getStreet(),
                delivery.getCity(),
                delivery.getState(),
                delivery.getZipCode(),
                delivery.getStatus(),
                delivery.getCreatedAt(),
                delivery.getDelivererName(),
                addressInfo
        );
    }
}

