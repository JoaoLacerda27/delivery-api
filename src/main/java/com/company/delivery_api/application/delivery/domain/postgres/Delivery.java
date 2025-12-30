package com.company.delivery_api.application.delivery.domain.postgres;

import com.company.delivery_api.application.delivery.domain.postgres.enums.DeliveryStatusEnum;
import com.company.delivery_api.shared.model.ModelBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Delivery extends ModelBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Order ID is mandatory")
    @Column(name = "order_id", nullable = false, unique = true, updatable = false)
    private UUID orderId;

    @NotBlank(message = "Street is mandatory")
    @Column(nullable = false, length = 255)
    private String street;

    @NotBlank(message = "City is mandatory")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State is mandatory")
    @Column(nullable = false, length = 50)
    private String state;

    @NotBlank(message = "Zip code is mandatory")
    @Column(name = "zip_code", nullable = false, length = 20)
    private String zipCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DeliveryStatusEnum status = DeliveryStatusEnum.PENDING;

    @PrePersist
    protected void prePersist() {
        if (this.status == null) {
            this.status = DeliveryStatusEnum.PENDING;
        }
    }
}

