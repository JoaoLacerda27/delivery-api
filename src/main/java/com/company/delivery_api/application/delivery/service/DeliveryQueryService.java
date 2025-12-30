package com.company.delivery_api.application.delivery.service;

import com.company.delivery_api.application.delivery.domain.mongo.AddressInfo;
import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import com.company.delivery_api.application.delivery.dto.TrackingEventResponse;
import com.company.delivery_api.application.delivery.repository.mongo.AddressInfoRepository;
import com.company.delivery_api.application.delivery.repository.mongo.DeliveryEventRepository;
import com.company.delivery_api.application.delivery.repository.postgres.DeliveryRepository;
import com.company.delivery_api.application.integrations.viacep.client.ViaCepClient;
import com.company.delivery_api.application.integrations.viacep.dto.ViaCepResponse;
import com.company.delivery_api.shared.exception.types.DeliveryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryQueryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryEventRepository eventRepository;
    private final AddressInfoRepository addressInfoRepository;
    private final ViaCepClient viaCepClient;

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

    public AddressInfo getOrFetchAddress(UUID deliveryId, String cep) {
        return addressInfoRepository.findByDeliveryId(deliveryId)
                .orElseGet(() -> {
                    ViaCepResponse response = viaCepClient.getAddress(cep);

                    AddressInfo info = AddressInfo.builder()
                            .deliveryId(deliveryId)
                            .cep(response.cep())
                            .street(response.logradouro())
                            .neighborhood(response.bairro())
                            .city(response.localidade())
                            .state(response.uf())
                            .fetchedAt(Instant.now())
                            .build();

                    return addressInfoRepository.save(info);
                });
    }
}

