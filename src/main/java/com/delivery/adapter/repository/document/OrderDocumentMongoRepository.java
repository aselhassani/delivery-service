package com.delivery.adapter.repository.document;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderDocumentMongoRepository extends MongoRepository<OrderDocument, String> {
}
