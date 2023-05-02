package com.example.back.repositories;

import com.example.back.domain.CardDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CardRepository extends MongoRepository<CardDocument, String> {

    Optional<CardDocument> findByAggregateId(String aggregateId);

    void deleteByAggregateId(String aggregateId);
}
