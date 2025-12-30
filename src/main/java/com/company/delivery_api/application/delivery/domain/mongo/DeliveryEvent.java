package com.company.delivery_api.application.delivery.domain.mongo;

import com.company.delivery_api.application.delivery.domain.mongo.enums.DeliveryEventTypeEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "delivery_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryEvent {

    @Id
    private String id;

    @Field("delivery_id")
    private UUID deliveryId;

    @Field("type")
    private DeliveryEventTypeEnum type;

    @Field("description")
    private String description;

    @Field("created_at")
    private Instant createdAt;

    public static DeliveryEvent of(UUID deliveryId,
                                   DeliveryEventTypeEnum type,
                                   String description) {
        return DeliveryEvent.builder()
                .deliveryId(deliveryId)
                .type(type)
                .description(description)
                .createdAt(Instant.now())
                .build();
    }
}

