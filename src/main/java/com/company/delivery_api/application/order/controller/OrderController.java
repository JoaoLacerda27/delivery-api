package com.company.delivery_api.application.order.controller;

import com.company.delivery_api.application.order.controller.doc.OrderDoc;
import com.company.delivery_api.application.order.domain.postgres.Order;
import com.company.delivery_api.application.order.domain.postgres.OrderItem;
import com.company.delivery_api.application.order.dto.CreateOrderRequest;
import com.company.delivery_api.application.order.dto.OrderItemResponse;
import com.company.delivery_api.application.order.dto.OrderResponse;
import com.company.delivery_api.application.order.repository.postgres.OrderItemRepository;
import com.company.delivery_api.application.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController implements OrderDoc {

    private final OrderService service;
    private final com.company.delivery_api.application.order.repository.postgres.OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @GetMapping
    @Operation(summary = "List all orders", description = "Returns a paginated list of all non-deleted orders")
    public ResponseEntity<Page<OrderResponse>> getAll(
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable
    ) {
        Page<Order> orders = orderRepository.findAllNotDeleted(pageable);
        Page<OrderResponse> response = orders.map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Override
    public ResponseEntity<OrderResponse> create(
            @RequestBody @Valid CreateOrderRequest request
    ) {
        Order order = service.create(
                request.customerId(),
                request.customerName(),
                request.totalAmount(),
                request.items()
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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Soft delete an order (marks as deleted, does not remove from database)")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<OrderItemResponse> items = orderItems.stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .toList();
        
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.getCustomerName(),
                order.getTotalAmount(),
                order.getStatus(),
                items,
                order.getCreatedAt()
        );
    }
}


