package com.delivery.adapter.kafka.dto;

import com.delivery.domain.model.Delivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DeliveryEventDto {

    private String orderId;
    private String slotId;
    private String deliveryStatus;

    public static DeliveryEventDto fromDomain(Delivery delivery) {
        return DeliveryEventDto.builder()
            .orderId(delivery.orderId())
            .slotId(delivery.slotId())
            .deliveryStatus(delivery.deliveryStatus().name())
            .build();
    }
}