package com.company.delivery_api.shared.exception.types;

import java.util.UUID;

public class DeliveryNotFoundException extends RuntimeException {
    
    public DeliveryNotFoundException(UUID deliveryId) {
        super("Delivery not found with id: " + deliveryId);
    }
    
    public DeliveryNotFoundException(String message) {
        super(message);
    }
}

