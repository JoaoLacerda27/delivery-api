package com.company.delivery_api.application.delivery.repository.postgres;

import com.company.delivery_api.application.delivery.domain.postgres.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    
    Optional<Delivery> findByOrderId(UUID orderId);
}

