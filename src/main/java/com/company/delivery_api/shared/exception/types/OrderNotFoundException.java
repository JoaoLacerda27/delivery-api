package com.company.delivery_api.shared.exception.types;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(UUID orderId) {
        super("Order not found with id: " + orderId);
    }
    
    public OrderNotFoundException(String message) {
        super(message);
    }
}

