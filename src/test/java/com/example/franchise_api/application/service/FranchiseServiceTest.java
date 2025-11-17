package com.example.franchise_api.application.service;

import com.example.franchise_api.domain.model.Franchise;
import com.example.franchise_api.domain.port.BranchRepositoryPort;
import com.example.franchise_api.domain.port.FranchiseRepositoryPort;
import com.example.franchise_api.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

class FranchiseServiceTest {

    private final FranchiseRepositoryPort franchiseRepo = Mockito.mock(FranchiseRepositoryPort.class);
    private final BranchRepositoryPort branchRepo = Mockito.mock(BranchRepositoryPort.class);
    private final ProductRepositoryPort productRepo = Mockito.mock(ProductRepositoryPort.class);

    private final FranchiseService service = new FranchiseService(franchiseRepo, branchRepo, productRepo);

    @Test
    void createFranchise_shouldReturnSavedFranchise() {
        Franchise franchise = Franchise.builder().id("1").name("Test Franchise").build();

        Mockito.when(franchiseRepo.save(any(Franchise.class)))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = service.createFranchise("Test Franchise");

        StepVerifier.create(result)
                .expectNextMatches(f -> f.getId().equals("1") && f.getName().equals("Test Franchise"))
                .verifyComplete();

        Mockito.verify(franchiseRepo).save(any(Franchise.class));
    }
}
