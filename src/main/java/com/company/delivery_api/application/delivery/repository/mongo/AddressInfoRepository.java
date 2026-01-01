package com.company.delivery_api.application.delivery.repository.mongo;

import com.company.delivery_api.application.delivery.domain.mongo.AddressInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressInfoRepository extends MongoRepository<AddressInfo, String> {

    Optional<AddressInfo> findByDeliveryId(UUID deliveryId);
    
    Optional<AddressInfo> findByCep(String cep);
}


