package com.example.franchise_api.infrastructure.persistence.mongo.adapter;

import com.example.franchise_api.domain.model.Product;
import com.example.franchise_api.domain.port.ProductRepositoryPort;
import com.example.franchise_api.infrastructure.persistence.mongo.document.ProductDocument;
import com.example.franchise_api.infrastructure.persistence.mongo.repository.ProductReactiveRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductReactiveRepository repository;

    public ProductRepositoryAdapter(ProductReactiveRepository repository) {
        this.repository = repository;
    }

    private ProductDocument toDocument(Product product) {
        return ProductDocument.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .branchId(product.getBranchId())
                .build();
    }

    private Product toDomain(ProductDocument doc) {
        return Product.builder()
                .id(doc.getId())
                .name(doc.getName())
                .stock(doc.getStock())
                .branchId(doc.getBranchId())
                .build();
    }

    @Override
    public Mono<Product> save(Product product) {
        return repository.save(toDocument(product))
                .map(this::toDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Product> findById(String id) {
        return repository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Flux<Product> findByBranchId(String branchId) {
        return repository.findByBranchId(branchId)
                .map(this::toDomain);
    }
}
