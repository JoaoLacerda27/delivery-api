package com.company.delivery_api.application.order.domain.postgres;

import com.company.delivery_api.application.customer.domain.postgres.Customer;
import com.company.delivery_api.application.order.domain.postgres.enums.OrderStatusEnum;
import com.company.delivery_api.shared.model.ModelBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Order extends ModelBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotNull(message = "Customer is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "FK_ORDER__CUSTOMER_ID"))
    private Customer customer;

    @NotNull(message = "Product ID is mandatory")
    @Column(name = "product_id", nullable = false)
    private Long productId; // ID do produto na Fake Store API

    @Min(value = 1, message = "Quantity must be at least 1")
    @NotNull(message = "Quantity is mandatory")
    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderStatusEnum status = OrderStatusEnum.CREATED;

    @PrePersist
    protected void prePersist() {
        if (this.status == null) {
            this.status = OrderStatusEnum.CREATED;
        }
    }
}

