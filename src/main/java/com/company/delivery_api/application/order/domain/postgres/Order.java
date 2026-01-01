package com.company.delivery_api.application.order.domain.postgres;

import com.company.delivery_api.application.order.domain.postgres.enums.OrderStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "customer_id", length = 100)
    private String customerId;

    @NotBlank(message = "Customer name is mandatory")
    @Column(name = "customer_name", nullable = false, length = 255)
    private String customerName;

    @NotNull(message = "Total amount is mandatory")
    @Positive(message = "Total amount must be positive")
    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private OrderStatusEnum status = OrderStatusEnum.CREATED;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @PrePersist
    void prePersist() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = OrderStatusEnum.CREATED;
        }
        if (this.deleted == null) {
            this.deleted = false;
        }
    }
}

