package com.delivery.adapter.repository;

import com.delivery.adapter.repository.document.OrderDocument;
import com.delivery.adapter.repository.document.OrderDocumentMongoRepository;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderMongoRepositoryTest {
  @InjectMocks
  private OrderMongoRepository underTest;
  @Mock
  private OrderDocumentMongoRepository orderDocumentMongoRepository;

  @Test
  void findByIdRetrievesOrderById() {

    var orderId = TestHelper.getRandomId();
    var orderDocument = DocumentHelper.getRandomOrderDocument();

    when(orderDocumentMongoRepository.findById(any())).thenReturn(Optional.of(orderDocument));

    var result = underTest.findById(orderId);

    verify(orderDocumentMongoRepository).findById(orderId);

    assertThat(result)
      .isPresent()
      .get()
      .usingRecursiveComparison()
      .isEqualTo(orderDocument);

  }

  @Test
  void saveShouldSaveTheOrderAndReturnIt() {

    var orderDocument = DocumentHelper.getRandomOrderDocument();
    var order = DomainHelper.getRandomOrder(TestHelper.getRandom(DeliveryStatus.class));

    when(orderDocumentMongoRepository.save(any())).thenReturn(orderDocument);

    var result = underTest.save(order);

    var captor = ArgumentCaptor.forClass(OrderDocument.class);
    verify(orderDocumentMongoRepository).save(captor.capture());

    var inOrderDocument = captor.getValue();

    assertThat(inOrderDocument)
      .usingRecursiveComparison()
      .isEqualTo(order);

    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(orderDocument);
  }

}
