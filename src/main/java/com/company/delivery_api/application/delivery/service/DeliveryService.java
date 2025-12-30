package com.company.delivery_api.application.delivery.service;

import com.company.delivery_api.application.delivery.domain.mongo.DeliveryEvent;
import com.company.delivery_api.application.delivery.domain.mongo.enums.DeliveryEventTypeEnum;
import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import com.company.delivery_api.application.delivery.domain.postgres.enums.DeliveryStatusEnum;
import com.company.delivery_api.application.delivery.repository.mongo.DeliveryEventRepository;
import com.company.delivery_api.application.delivery.repository.postgres.DeliveryRepository;
import com.company.delivery_api.application.order.repository.postgres.OrderRepository;
import com.company.delivery_api.shared.exception.types.DeliveryAlreadyExistsException;
import com.company.delivery_api.shared.exception.types.DeliveryNotFoundException;
import com.company.delivery_api.shared.exception.types.InvalidDeliveryStatusTransitionException;
import com.company.delivery_api.shared.exception.types.OrderNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventRepository deliveryEventRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Delivery createDelivery(UUID orderId, String street, String city, String state, String zipCode) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException(orderId);
        }

        if (deliveryRepository.findByOrderId(orderId).isPresent()) {
            throw new DeliveryAlreadyExistsException(orderId);
        }

        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .street(street)
                .city(city)
                .state(state)
                .zipCode(zipCode)
                .status(DeliveryStatusEnum.PENDING)
                .build();

        Delivery saved = deliveryRepository.save(delivery);

        deliveryEventRepository.save(
                DeliveryEvent.of(
                        saved.getId(),
                        DeliveryEventTypeEnum.CREATED,
                        "Delivery created for order " + orderId
                )
        );

        return saved;
    }

    @Transactional
    public Delivery updateStatus(UUID deliveryId, DeliveryStatusEnum status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));

        if (status == DeliveryStatusEnum.PENDING) {
            throw new InvalidDeliveryStatusTransitionException(
                    "Cannot update delivery status to PENDING. PENDING is only set during delivery creation."
            );
        }

        delivery.setStatus(status);
        Delivery updated = deliveryRepository.save(delivery);

        DeliveryEventTypeEnum eventType = status.toEvent();

        deliveryEventRepository.save(
                DeliveryEvent.of(
                        deliveryId,
                        eventType,
                        "Status updated to " + status
                )
        );

        return updated;
    }
}

