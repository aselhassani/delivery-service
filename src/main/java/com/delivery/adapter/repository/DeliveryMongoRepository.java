package com.delivery.adapter.repository;

import com.delivery.adapter.repository.document.DeliveryDocument;
import com.delivery.adapter.repository.document.DeliveryDocumentMongoRepository;
import com.delivery.domain.model.Delivery;
import com.delivery.port.repository.DeliveryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeliveryMongoRepository implements DeliveryRepository {

  private final DeliveryDocumentMongoRepository deliveryDocumentMongoRepository;
  @Override
  public Delivery save(Delivery delivery) {
    return Optional.of(delivery)
      .map(DeliveryDocument::fromDomain)
      .map(deliveryDocumentMongoRepository::save)
      .map(DeliveryDocument::toDomain)
      .orElse(null);
  }

}
