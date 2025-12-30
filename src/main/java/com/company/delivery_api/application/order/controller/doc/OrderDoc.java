package com.company.delivery_api.application.order.controller.doc;

import com.company.delivery_api.application.order.dto.CreateOrderRequest;
import com.company.delivery_api.application.order.dto.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Orders", description = "Order management API")
public interface OrderDoc {

    @Operation(
            summary = "Create a new order",
            description = "Creates a new order with customer name and total amount"
    )
    ResponseEntity<OrderResponse> create(CreateOrderRequest request);

    @Operation(
            summary = "Get order by ID",
            description = "Returns order details by ID"
    )
    ResponseEntity<OrderResponse> getById(
            @Parameter(description = "Order ID") UUID id
    );
}

