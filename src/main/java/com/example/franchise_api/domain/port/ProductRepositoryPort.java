package com.example.franchise_api.domain.port;

import com.example.franchise_api.domain.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepositoryPort {

    Mono<Product> save(Product product);

    Mono<Void> deleteById(String id);

    Mono<Product> findById(String id);

    Flux<Product> findByBranchId(String branchId);
}
