package com.company.delivery_api.shared.exception;

import com.company.delivery_api.shared.exception.response.ErrorResponse;
import com.company.delivery_api.shared.exception.types.DeliveryAlreadyExistsException;
import com.company.delivery_api.shared.exception.types.DeliveryNotFoundException;
import com.company.delivery_api.shared.exception.types.InvalidDeliveryStatusTransitionException;
import com.company.delivery_api.shared.exception.types.OrderNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryNotFound(DeliveryNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "DELIVERY_NOT_FOUND", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DeliveryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryAlreadyExists(DeliveryAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "DELIVERY_ALREADY_EXISTS", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(InvalidDeliveryStatusTransitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDeliveryStatusTransition(InvalidDeliveryStatusTransitionException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_DELIVERY_STATUS_TRANSITION", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, "INVALID_STATE", ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String error, String message, String path) {
        ErrorResponse errorResponse = ErrorResponse.of(status.value(), error, message, path);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

