package com.company.delivery_api.application.delivery.domain.mongo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;

@Document(collection = "address_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressInfo {

    @Id
    private String id;

    @Field("delivery_id")
    private UUID deliveryId;

    @Field("cep")
    private String cep;

    @Field("street")
    private String street;

    @Field("neighborhood")
    private String neighborhood;

    @Field("city")
    private String city;

    @Field("state")
    private String state;

    @Field("fetched_at")
    private Instant fetchedAt;
}

