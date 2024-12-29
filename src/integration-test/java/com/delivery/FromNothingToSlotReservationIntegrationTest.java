package com.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.delivery.adapter.repository.document.DeliverySlotDocument;
import com.delivery.adapter.repository.document.DeliverySlotDocumentMongoRepository;
import com.delivery.adapter.repository.document.DeliveryStatusDocument;
import com.delivery.adapter.repository.document.OrderDocument;
import com.delivery.adapter.repository.document.OrderDocumentMongoRepository;
import com.delivery.adapter.ws.dto.DeliveryDto;
import com.delivery.adapter.ws.dto.DeliveryMode;
import com.delivery.adapter.ws.dto.DeliverySlotDto;
import com.delivery.adapter.ws.dto.DeliveryStatus;
import com.delivery.test.helper.DocumentHelper;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
    properties = {"server.port=8042"}
)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9098", "port=9098" })
class FromNothingToSlotReservationIntegrationTest {

  @Autowired
  private OrderDocumentMongoRepository orderDocumentMongoRepository;
  @Autowired
  private DeliverySlotDocumentMongoRepository deliverySlotDocumentMongoRepository;
  @Autowired
  private ApiClient apiClient;
  private OrderDocument orderDocument;
  private DeliverySlotDocument slotDocument;

  private static final String bootstrapAddress = "localhost:9098";
  private static final String topic = "delivery.event";

  @BeforeEach
  void setup() {
    orderDocument = orderDocumentMongoRepository.save(DocumentHelper.getRandomOrderDocument(DeliveryStatusDocument.PENDING));
    slotDocument = deliverySlotDocumentMongoRepository.save(DocumentHelper.getRandomDeliverySlotDocument(false));
  }

  @Test
  void testCompleteSlotReservationWorkflow() {

    var deliverySlotDto = apiClient.getDeliverySlot(DeliveryMode.valueOf(slotDocument.getDeliveryMode().name()));
    assertDeliverySlotMatchesExpected(deliverySlotDto);

    var deliveryDto = apiClient.reserveDeliverySlot(orderDocument.getOrderId(), slotDocument.getSlotId());

    assertSlotReserved();
    assertOrderScheduled();
    assertDeliveryEventPublished();
    assertDeliveryDtoMatchesExpected(deliveryDto);
  }

  private void assertDeliveryEventPublished() {
    //TODO implement kafka delivery event consumption
  }

  private void assertSlotReserved() {
    assertThat(deliverySlotDocumentMongoRepository.findById(slotDocument.getSlotId()))
        .isPresent()
        .get()
        .extracting("reserved")
        .isEqualTo(true);
  }

  private void assertOrderScheduled() {
    assertThat(orderDocumentMongoRepository.findById(orderDocument.getOrderId()))
        .isPresent()
        .get()
        .extracting("deliveryStatus")
        .isEqualTo(DeliveryStatusDocument.SCHEDULED);
  }

  private void assertDeliverySlotMatchesExpected(DeliverySlotDto deliverySlotDto) {

    assertThat(deliverySlotDto)
        .usingRecursiveComparison()
        .ignoringFields("reserved", "start", "end")
        .isEqualTo(slotDocument);

    assertThat(deliverySlotDto.start()).isCloseTo(slotDocument.getStart().atOffset(ZoneOffset.UTC), within(1, ChronoUnit.MILLIS));
    assertThat(deliverySlotDto.end()).isCloseTo(slotDocument.getEnd().atOffset(ZoneOffset.UTC), within(1, ChronoUnit.MILLIS));
  }

  private void assertDeliveryDtoMatchesExpected(DeliveryDto dto) {
    assertThat(dto.deliveryId()).isNotBlank();
    assertThat(dto.slotId()).isEqualTo(slotDocument.getSlotId());
    assertThat(dto.orderId()).isEqualTo(orderDocument.getOrderId());
    assertThat(dto.deliveryStatus()).isEqualTo(DeliveryStatus.SCHEDULED);
    assertThat(dto.deliveryMode()).isEqualTo(DeliveryMode.valueOf(slotDocument.getDeliveryMode().name()));
  }

}