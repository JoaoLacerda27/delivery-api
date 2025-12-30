package com.company.delivery_api.shared.exception.types;

public class InvalidDeliveryStatusTransitionException extends RuntimeException {
    
    public InvalidDeliveryStatusTransitionException(String message) {
        super(message);
    }
}

