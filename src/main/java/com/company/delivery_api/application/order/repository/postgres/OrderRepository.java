package com.company.delivery_api.application.order.repository.postgres;

import com.company.delivery_api.application.order.domain.postgres.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    @Query("SELECT o FROM Order o WHERE o.deleted = false")
    Page<Order> findAllNotDeleted(Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.id = :id AND o.deleted = false")
    Optional<Order> findByIdNotDeleted(@Param("id") UUID id);
}


