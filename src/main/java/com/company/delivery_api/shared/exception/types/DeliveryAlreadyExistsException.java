package com.company.delivery_api.shared.exception.types;

import java.util.UUID;

public class DeliveryAlreadyExistsException extends RuntimeException {
    
    public DeliveryAlreadyExistsException(UUID orderId) {
        super("Delivery already exists for order with id: " + orderId);
    }
    
    public DeliveryAlreadyExistsException(String message) {
        super(message);
    }
}

