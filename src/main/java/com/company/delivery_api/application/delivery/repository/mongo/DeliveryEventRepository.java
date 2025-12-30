package com.company.delivery_api.application.delivery.repository.mongo;

import com.company.delivery_api.application.delivery.domain.mongo.DeliveryEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryEventRepository extends MongoRepository<DeliveryEvent, String> {
    
    List<DeliveryEvent> findByDeliveryIdOrderByOccurredAtAsc(UUID deliveryId);
}

