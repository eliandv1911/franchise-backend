package com.example.franchise_api.infrastructure.persistence.mongo.repository;

import com.example.franchise_api.infrastructure.persistence.mongo.document.FranchiseDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FranchiseReactiveRepository extends ReactiveMongoRepository<FranchiseDocument, String> {
}
