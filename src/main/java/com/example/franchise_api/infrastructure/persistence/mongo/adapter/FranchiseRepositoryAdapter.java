package com.example.franchise_api.infrastructure.persistence.mongo.adapter;

import com.example.franchise_api.domain.model.Franchise;
import com.example.franchise_api.domain.port.FranchiseRepositoryPort;
import com.example.franchise_api.infrastructure.persistence.mongo.document.FranchiseDocument;
import com.example.franchise_api.infrastructure.persistence.mongo.repository.FranchiseReactiveRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class FranchiseRepositoryAdapter implements FranchiseRepositoryPort {

    private final FranchiseReactiveRepository repository;

    public FranchiseRepositoryAdapter(FranchiseReactiveRepository repository) {
        this.repository = repository;
    }

    private FranchiseDocument toDocument(Franchise franchise) {
        return FranchiseDocument.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .build();
    }

    private Franchise toDomain(FranchiseDocument doc) {
        return Franchise.builder()
                .id(doc.getId())
                .name(doc.getName())
                .build();
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return repository.save(toDocument(franchise))
                .map(this::toDomain);
    }

    @Override
    public Mono<Franchise> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<Franchise> findAll() {
        return repository.findAll()
                .map(this::toDomain);
    }
}
