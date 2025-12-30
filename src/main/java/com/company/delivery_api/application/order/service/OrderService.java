package com.company.delivery_api.application.order.service;

import com.company.delivery_api.application.order.domain.postgres.Order;
import com.company.delivery_api.application.order.repository.postgres.OrderRepository;
import com.company.delivery_api.shared.exception.types.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public Order create(String customerName, BigDecimal totalAmount) {
        Order order = Order.builder()
                .customerName(customerName)
                .totalAmount(totalAmount)
                .build();

        return repository.save(order);
    }

    public Order findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}

