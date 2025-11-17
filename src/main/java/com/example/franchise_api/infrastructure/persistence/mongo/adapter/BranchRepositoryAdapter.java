package com.example.franchise_api.infrastructure.persistence.mongo.adapter;

import com.example.franchise_api.domain.model.Branch;
import com.example.franchise_api.domain.port.BranchRepositoryPort;
import com.example.franchise_api.infrastructure.persistence.mongo.document.BranchDocument;
import com.example.franchise_api.infrastructure.persistence.mongo.repository.BranchReactiveRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BranchRepositoryAdapter implements BranchRepositoryPort {

    private final BranchReactiveRepository repository;

    public BranchRepositoryAdapter(BranchReactiveRepository repository) {
        this.repository = repository;
    }

    private BranchDocument toDocument(Branch branch) {
        return BranchDocument.builder()
                .id(branch.getId())
                .name(branch.getName())
                .franchiseId(branch.getFranchiseId())
                .build();
    }

    private Branch toDomain(BranchDocument doc) {
        return Branch.builder()
                .id(doc.getId())
                .name(doc.getName())
                .franchiseId(doc.getFranchiseId())
                .build();
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return repository.save(toDocument(branch))
                .map(this::toDomain);
    }

    @Override
    public Mono<Branch> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<Branch> findByFranchiseId(String franchiseId) {
        return repository.findByFranchiseId(franchiseId)
                .map(this::toDomain);
    }
}
