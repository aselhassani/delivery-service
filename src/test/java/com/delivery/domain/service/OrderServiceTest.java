package com.delivery.domain.service;

import com.delivery.adapter.repository.OrderMongoRepository;
import com.delivery.domain.model.DeliveryStatus;
import com.delivery.test.helper.DomainHelper;
import com.delivery.test.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @InjectMocks
  private OrderService underTest;

  @Mock
  private OrderMongoRepository repository;

  @Test
  void findByIdRetrievesOrderById() {

    var orderId = TestHelper.getRandomId();
    var order = DomainHelper.getRandomOrder(TestHelper.getRandom(DeliveryStatus.class));

    when(repository.findById(any())).thenReturn(Optional.of(order));

    var result = underTest.findById(orderId);

    verify(repository).findById(orderId);
    assertThat(result).contains(order);
  }



}
