package com.delivery.adapter.repository.document;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeliverySlotDocumentMongoRepository extends MongoRepository<DeliverySlotDocument, String> {
  List<DeliverySlotDocument> findByReservedFalse();
}
