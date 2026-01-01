package com.company.delivery_api.application.order.service;

import com.company.delivery_api.application.order.domain.postgres.Order;
import com.company.delivery_api.application.order.domain.postgres.OrderItem;
import com.company.delivery_api.application.order.domain.postgres.enums.OrderStatusEnum;
import com.company.delivery_api.application.order.repository.postgres.OrderItemRepository;
import com.company.delivery_api.application.order.repository.postgres.OrderRepository;
import com.company.delivery_api.shared.exception.types.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderItemRepository orderItemRepository;

    public Order create(String customerName, BigDecimal totalAmount) {
        Order order = Order.builder()
                .customerName(customerName)
                .totalAmount(totalAmount)
                .build();

        return repository.save(order);
    }
    
    @Transactional
    public Order create(String customerId, String customerName, BigDecimal totalAmount, List<com.company.delivery_api.application.order.dto.OrderItemRequest> items) {
        BigDecimal calculatedTotal = totalAmount;
        if (calculatedTotal == null && items != null && !items.isEmpty()) {
            calculatedTotal = items.stream()
                    .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        
        if (calculatedTotal == null) {
            throw new IllegalArgumentException("Total amount is required. Provide either totalAmount or items.");
        }
        
        Order order = Order.builder()
                .customerId(customerId)
                .customerName(customerName)
                .totalAmount(calculatedTotal)
                .build();

        Order savedOrder = repository.save(order);
        
        if (items != null && !items.isEmpty()) {
            List<OrderItem> orderItems = items.stream()
                    .map(item -> OrderItem.builder()
                            .order(savedOrder)
                            .productName(item.productName())
                            .quantity(item.quantity())
                            .price(item.price())
                            .build())
                    .toList();
            orderItemRepository.saveAll(orderItems);
        }

        return savedOrder;
    }

    public Order findById(UUID id) {
        return repository.findByIdNotDeleted(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
    
    @Transactional
    public void delete(UUID id) {
        Order order = repository.findByIdNotDeleted(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        order.setDeleted(true);
        repository.save(order);
    }
    
    @Transactional
    public void updateStatus(UUID orderId, OrderStatusEnum status) {
        Order order = repository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(status);
        repository.save(order);
    }
}


