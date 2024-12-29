package com.delivery.adapter.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.delivery.adapter.repository.document.DeliverySlotDocument;
import com.delivery.adapter.repository.document.DeliverySlotDocumentMongoRepository;
import com.delivery.domain.model.DeliveryMode;
import com.delivery.domain.model.DeliverySlot;
import com.delivery.test.helper.DocumentHelper;
import com.delivery.test.helper.DomainHelper;
import com.delivery.test.helper.TestHelper;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliverySlotMongoRepositoryTest {
  @InjectMocks
  private DeliverySlotMongoRepository underTest;
  @Mock
  private DeliverySlotDocumentMongoRepository deliverySlotDocumentMongoRepository;

  @Test
  void findByIdRetrievesDeliverySlotById() {

    var slotId = TestHelper.getRandomId();
    var deliverySlotDocument = DocumentHelper.getRandomDeliverySlotDocument();

    when(deliverySlotDocumentMongoRepository.findById(any())).thenReturn(Optional.of(deliverySlotDocument));

    var result = underTest.findById(slotId);

    verify(deliverySlotDocumentMongoRepository).findById(slotId);

    assertThat(result)
      .isPresent()
      .get()
      .usingRecursiveComparison()
      .ignoringFields("start","end")
      .isEqualTo(deliverySlotDocument);

    assertThat(result)
        .get()
        .extracting("start", "end")
        .containsExactly(
            deliverySlotDocument.getStart().atOffset(ZoneOffset.UTC),
            deliverySlotDocument.getEnd().atOffset(ZoneOffset.UTC)
        );
  }

  @Test
  void getAvailableSlotRetrievesNonReservedSlots() {

    var deliveryMode = TestHelper.getRandom(DeliveryMode.class);
    var deliverySlotDocument = DocumentHelper.getRandomDeliverySlotDocument();

    when(deliverySlotDocumentMongoRepository.findByReservedFalse()).thenReturn(List.of(deliverySlotDocument));

    var result = underTest.getAvailableDeliverySlots(deliveryMode);

    assertThat(result).hasSize(1);
    assertDocumentToDomainSlotMapping(deliverySlotDocument, result.getFirst());
  }

  @Test
  void saveShouldSaveTheSlotAndReturnIt() {

    var deliverySlotDocument = DocumentHelper.getRandomDeliverySlotDocument();
    var slot = DomainHelper.getRandomDeliverySlot();

    when(deliverySlotDocumentMongoRepository.save(any())).thenReturn(deliverySlotDocument);

    var result = underTest.save(slot);

    var captor = ArgumentCaptor.forClass(DeliverySlotDocument.class);
    verify(deliverySlotDocumentMongoRepository).save(captor.capture());

    var inSlotDocument = captor.getValue();

    assertDocumentToDomainSlotMapping(inSlotDocument, slot);
    assertDocumentToDomainSlotMapping(deliverySlotDocument, result);

  }

  private void assertDocumentToDomainSlotMapping(DeliverySlotDocument inSlotDocument, DeliverySlot slot) {

    assertThat(inSlotDocument)
        .usingRecursiveComparison()
        .ignoringFields("start", "end")
        .isEqualTo(slot);

    assertThat(inSlotDocument)
        .extracting("start", "end")
        .containsExactly(slot.start().toInstant(), slot.end().toInstant());
  }

}
