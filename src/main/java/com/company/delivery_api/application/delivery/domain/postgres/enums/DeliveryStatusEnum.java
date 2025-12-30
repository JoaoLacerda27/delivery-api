package com.company.delivery_api.application.delivery.domain.postgres.enums;

import com.company.delivery_api.application.delivery.domain.mongo.enums.DeliveryEventTypeEnum;

public enum DeliveryStatusEnum {
    PENDING(DeliveryEventTypeEnum.CREATED),
    IN_TRANSIT(DeliveryEventTypeEnum.IN_TRANSIT),
    DELIVERED(DeliveryEventTypeEnum.DELIVERED),
    FAILED(DeliveryEventTypeEnum.FAILED);

    private final DeliveryEventTypeEnum eventType;

    DeliveryStatusEnum(DeliveryEventTypeEnum eventType) {
        this.eventType = eventType;
    }

    public DeliveryEventTypeEnum toEvent() {
        return eventType;
    }
}

