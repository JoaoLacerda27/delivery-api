package com.company.delivery_api.application.delivery.service;

import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import com.company.delivery_api.application.delivery.dto.TrackingEventResponse;
import com.company.delivery_api.application.delivery.repository.mongo.DeliveryEventRepository;
import com.company.delivery_api.application.delivery.repository.postgres.DeliveryRepository;
import com.company.delivery_api.shared.exception.types.DeliveryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryQueryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventRepository eventRepository;

    public Delivery findById(UUID id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new DeliveryNotFoundException(id));
    }

    public List<TrackingEventResponse> findEvents(UUID deliveryId) {
        return eventRepository.findByDeliveryIdOrderByOccurredAtAsc(deliveryId)
                .stream()
                .map(e -> new TrackingEventResponse(
                        e.getDescription(),
                        e.getSource() != null ? e.getSource() : "SYSTEM",
                        e.getOccurredAt() != null ? e.getOccurredAt() : e.getCreatedAt()
                ))
                .toList();
    }
}

