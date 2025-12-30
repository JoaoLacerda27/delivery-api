package com.company.delivery_api.shared.exception;

import com.company.delivery_api.shared.exception.response.ErrorResponse;
import com.company.delivery_api.shared.exception.types.DeliveryAlreadyExistsException;
import com.company.delivery_api.shared.exception.types.DeliveryNotFoundException;
import com.company.delivery_api.shared.exception.types.InvalidDeliveryStatusTransitionException;
import com.company.delivery_api.shared.exception.types.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryNotFound(DeliveryNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "DELIVERY_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(DeliveryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryAlreadyExists(DeliveryAlreadyExistsException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "DELIVERY_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(InvalidDeliveryStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDeliveryStatusTransition(InvalidDeliveryStatusTransitionException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_DELIVERY_STATUS_TRANSITION", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "INVALID_STATE", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String errorCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

