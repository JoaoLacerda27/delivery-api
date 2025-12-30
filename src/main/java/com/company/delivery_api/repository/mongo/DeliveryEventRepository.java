package com.company.delivery_api.repository.mongo;

import com.company.delivery_api.domain.mongo.DeliveryEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryEventRepository extends MongoRepository<DeliveryEvent, String> {
    
    List<DeliveryEvent> findByDeliveryIdOrderByCreatedAtAsc(UUID deliveryId);
}


