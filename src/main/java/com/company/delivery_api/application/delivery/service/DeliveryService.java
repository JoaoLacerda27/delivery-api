package com.company.delivery_api.application.delivery.service;

import com.company.delivery_api.application.delivery.domain.mongo.DeliveryEvent;
import com.company.delivery_api.application.delivery.domain.mongo.enums.DeliveryEventTypeEnum;
import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import com.company.delivery_api.application.delivery.domain.postgres.enums.DeliveryStatusEnum;
import com.company.delivery_api.application.delivery.repository.mongo.DeliveryEventRepository;
import com.company.delivery_api.application.delivery.repository.postgres.DeliveryRepository;
import com.company.delivery_api.application.order.domain.postgres.enums.OrderStatusEnum;
import com.company.delivery_api.application.order.repository.postgres.OrderRepository;
import com.company.delivery_api.application.order.service.OrderService;
import com.company.delivery_api.shared.exception.types.DeliveryAlreadyExistsException;
import com.company.delivery_api.shared.exception.types.DeliveryNotFoundException;
import com.company.delivery_api.shared.exception.types.InvalidDeliveryStatusTransitionException;
import com.company.delivery_api.shared.exception.types.OrderNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventRepository deliveryEventRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

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
    public Delivery updateStatus(UUID deliveryId, DeliveryStatusEnum status, Authentication authentication) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));

        if (status == DeliveryStatusEnum.PENDING) {
            throw new InvalidDeliveryStatusTransitionException(
                    "Cannot update delivery status to PENDING. PENDING is only set during delivery creation."
            );
        }

        if (status == DeliveryStatusEnum.IN_TRANSIT && authentication != null) {
            String delivererName = extractNameFromOAuth2(authentication);
            if (delivererName != null && !delivererName.isEmpty()) {
                delivery.setDelivererName(delivererName);
            }
        }

        delivery.setStatus(status);
        Delivery updated = deliveryRepository.save(delivery);

        updateOrderStatusFromDeliveryStatus(delivery.getOrderId(), status);

        DeliveryEventTypeEnum eventType = status.toEvent();
        String eventDescription = "Status updated to " + status;
        if (status == DeliveryStatusEnum.IN_TRANSIT && delivery.getDelivererName() != null) {
            eventDescription += " by " + delivery.getDelivererName();
        }

        deliveryEventRepository.save(
                DeliveryEvent.of(
                        deliveryId,
                        eventType,
                        eventDescription
                )
        );

        return updated;
    }

    private String extractNameFromOAuth2(Authentication authentication) {
        if (authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            return oauth2User.getAttribute("name");
        }
        return null;
    }

    private void updateOrderStatusFromDeliveryStatus(UUID orderId, DeliveryStatusEnum deliveryStatus) {
        OrderStatusEnum orderStatus = switch (deliveryStatus) {
            case IN_TRANSIT -> OrderStatusEnum.SHIPPED;
            case DELIVERED -> OrderStatusEnum.DELIVERED;
            case FAILED -> OrderStatusEnum.CANCELED;
            case PENDING -> null;
        };

        if (orderStatus != null) {
            orderService.updateStatus(orderId, orderStatus);
        }
    }
}

