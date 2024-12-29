package com.delivery.adapter.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.delivery.adapter.kafka.dto.DeliveryEventDto;
import com.delivery.domain.model.Delivery;
import com.delivery.domain.model.DeliveryStatus;
import com.delivery.test.helper.DomainHelper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class KafkaDeliveryEventPublisherTest {
  public static final String DELIVERY_EVENT_KAFKA_TOPIC = "delivery.event";
  @InjectMocks
  private KafkaDeliveryEventPublisher underTest;
  @Mock
  private KafkaTemplate<String, DeliveryEventDto> kafkaTemplate;

  @BeforeEach
  public void setup() {
    ReflectionTestUtils.setField(underTest, "topic", DELIVERY_EVENT_KAFKA_TOPIC);
  }

  @Test
  void publishDeliveryEventShouldSendKafkaMessage() {

    var delivery = DomainHelper.getRandomDelivery(DeliveryStatus.SCHEDULED);

    underTest.publishDeliveryEvent(delivery);

    var captor = ArgumentCaptor.forClass(ProducerRecord.class);

    verify(kafkaTemplate).send(captor.capture());

    ProducerRecord<String, DeliveryEventDto> record = captor.getValue();

    assertThat(record.topic()).isEqualTo(DELIVERY_EVENT_KAFKA_TOPIC);
    assertDomainToKafkaEventDtoMapping(delivery, record.value());

  }

  private void assertDomainToKafkaEventDtoMapping(Delivery domain, DeliveryEventDto eventDto) {
    assertThat(eventDto.getOrderId()).isEqualTo(domain.orderId());
    assertThat(eventDto.getSlotId()).isEqualTo(domain.slotId());
    assertThat(eventDto.getDeliveryStatus()).isEqualTo(domain.deliveryStatus().name());
  }
}
