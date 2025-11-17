package com.example.franchise_api.application.service;

import com.example.franchise_api.domain.model.Branch;
import com.example.franchise_api.domain.model.Franchise;
import com.example.franchise_api.domain.model.Product;
import com.example.franchise_api.domain.port.BranchRepositoryPort;
import com.example.franchise_api.domain.port.FranchiseRepositoryPort;
import com.example.franchise_api.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FranchiseService {

    private final FranchiseRepositoryPort franchiseRepository;
    private final BranchRepositoryPort branchRepository;
    private final ProductRepositoryPort productRepository;

    // 2. Nueva franquicia
    public Mono<Franchise> createFranchise(String name) {
        Franchise franchise = Franchise.builder()
                .name(name)
                .build();
        return franchiseRepository.save(franchise);
    }

    // Plus: actualizar nombre franquicia
    public Mono<Franchise> updateFranchiseName(String franchiseId, String newName) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(f -> {
                    f.setName(newName);
                    return franchiseRepository.save(f);
                });
    }

    // 3. Nueva sucursal
    public Mono<Branch> addBranchToFranchise(String franchiseId, String name) {
        return franchiseRepository.findById(franchiseId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Franchise not found")))
                .flatMap(f -> {
                    Branch branch = Branch.builder()
                            .name(name)
                            .franchiseId(f.getId())
                            .build();
                    return branchRepository.save(branch);
                });
    }

    // Plus: actualizar nombre sucursal
    public Mono<Branch> updateBranchName(String branchId, String newName) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Branch not found")))
                .flatMap(b -> {
                    b.setName(newName);
                    return branchRepository.save(b);
                });
    }

    // 4. Agregar producto a sucursal
    public Mono<Product> addProductToBranch(String branchId, String name, Integer stock) {
        return branchRepository.findById(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Branch not found")))
                .flatMap(b -> {
                    Product product = Product.builder()
                            .name(name)
                            .stock(stock)
                            .branchId(b.getId())
                            .build();
                    return productRepository.save(product);
                });
    }

    // Plus: actualizar nombre producto
    public Mono<Product> updateProductName(String productId, String newName) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")))
                .flatMap(p -> {
                    p.setName(newName);
                    return productRepository.save(p);
                });
    }

    // 5. Eliminar producto
    public Mono<Void> deleteProductFromBranch(String branchId, String productId) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")))
                .flatMap(p -> {
                    if (!p.getBranchId().equals(branchId)) {
                        return Mono.error(new IllegalArgumentException("Product does not belong to this branch"));
                    }
                    return productRepository.deleteById(productId);
                });
    }

    // 6. Modificar stock
    public Mono<Product> updateProductStock(String productId, Integer stock) {
        return productRepository.findById(productId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")))
                .flatMap(p -> {
                    p.setStock(stock);
                    return productRepository.save(p);
                });
    }

    // 7. Producto con más stock por sucursal en franquicia
    public Flux<Product> getTopProductsByBranchForFranchise(String franchiseId) {
        // Por cada sucursal de la franquicia, obtener sus productos y escoger el(s) de mayor stock
        return branchRepository.findByFranchiseId(franchiseId)
                .flatMap(branch ->
                        productRepository.findByBranchId(branch.getId())
                                .collectList()
                                .flatMapMany(products -> {
                                    if (products.isEmpty()) return Flux.empty();

                                    int maxStock = products.stream()
                                            .map(Product::getStock)
                                            .max(Comparator.naturalOrder())
                                            .orElse(0);

                                    return Flux.fromIterable(
                                            products.stream()
                                                    .filter(p -> p.getStock().equals(maxStock))
                                                    .toList()
                                    );
                                })
                );
    }
}
