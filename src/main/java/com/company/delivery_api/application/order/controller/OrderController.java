package com.company.delivery_api.application.order.controller;

import com.company.delivery_api.application.order.controller.doc.OrderDoc;
import com.company.delivery_api.application.order.domain.postgres.Order;
import com.company.delivery_api.application.order.dto.CreateOrderRequest;
import com.company.delivery_api.application.order.dto.OrderResponse;
import com.company.delivery_api.application.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController implements OrderDoc {

    private final OrderService service;

    @PostMapping
    @Override
    public ResponseEntity<OrderResponse> create(
            @RequestBody @Valid CreateOrderRequest request
    ) {
        Order order = service.create(
                request.customerName(),
                request.totalAmount()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(order));
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<OrderResponse> getById(
            @PathVariable UUID id
    ) {
        Order order = service.findById(id);
        return ResponseEntity.ok(toResponse(order));
    }

    private OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }
}

