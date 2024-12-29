package com.delivery.adapter.repository;

import com.delivery.adapter.repository.document.DeliveryDocument;
import com.delivery.adapter.repository.document.DeliveryDocumentMongoRepository;
import com.delivery.domain.model.DeliveryStatus;
import com.delivery.test.helper.DocumentHelper;
import com.delivery.test.helper.DomainHelper;
import com.delivery.test.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryMongoRepositoryTest {

  @InjectMocks
  private DeliveryMongoRepository underTest;
  @Mock
  private DeliveryDocumentMongoRepository deliveryDocumentMongoRepository;

  @Test
  void saveShouldSaveTheDeliveryAndReturnIt() {

    var deliveryDocument = DocumentHelper.getRandomDeliveryDocument();
    var delivery = DomainHelper.getRandomDelivery(TestHelper.getRandom(DeliveryStatus.class));

    when(deliveryDocumentMongoRepository.save(any())).thenReturn(deliveryDocument);

    var result = underTest.save(delivery);

    var captor = ArgumentCaptor.forClass(DeliveryDocument.class);
    verify(deliveryDocumentMongoRepository).save(captor.capture());

    var inDeliveryDocument = captor.getValue();

    assertThat(inDeliveryDocument)
      .usingRecursiveComparison()
      .isEqualTo(delivery);

    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(deliveryDocument);
  }

}
