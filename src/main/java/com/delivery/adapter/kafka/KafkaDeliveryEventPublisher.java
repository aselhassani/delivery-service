package com.delivery.adapter.kafka;

import com.delivery.adapter.kafka.dto.DeliveryEventDto;
import com.delivery.domain.model.Delivery;
import com.delivery.port.publisher.DeliveryEventPublisher;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDeliveryEventPublisher implements
    DeliveryEventPublisher {

  private final KafkaTemplate<String, DeliveryEventDto> kafkaTemplate;

  @Value("${spring.kafka.topics.delivery-event}")
  private String topic;

  @Override
  public void publishDeliveryEvent(Delivery delivery) {
    Optional.of(delivery)
      .map(DeliveryEventDto::fromDomain)
      .map(event -> new ProducerRecord<String, DeliveryEventDto>(topic, event))
      .ifPresent(kafkaTemplate::send);
  }
}
