package com.example.franchise_api.infrastructure.web.controller;

import com.example.franchise_api.application.service.FranchiseService;
import com.example.franchise_api.domain.model.Product;
import com.example.franchise_api.infrastructure.web.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

    private final FranchiseService franchiseService;

    // 2. Crear franquicia
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FranchiseResponse> createFranchise(@Valid @RequestBody FranchiseRequest request) {
        return franchiseService.createFranchise(request.name())
                .map(f -> new FranchiseResponse(f.getId(), f.getName()));
    }

    // Plus: actualizar nombre franquicia
    @PutMapping("/{franchiseId}")
    public Mono<FranchiseResponse> updateFranchiseName(@PathVariable String franchiseId,
                                                       @Valid @RequestBody FranchiseRequest request) {
        return franchiseService.updateFranchiseName(franchiseId, request.name())
                .map(f -> new FranchiseResponse(f.getId(), f.getName()));
    }

    // 3. Agregar sucursal a franquicia
    @PostMapping("/{franchiseId}/branches")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BranchResponse> addBranchToFranchise(@PathVariable String franchiseId,
                                                     @Valid @RequestBody BranchRequest request) {
        return franchiseService.addBranchToFranchise(franchiseId, request.name())
                .map(b -> new BranchResponse(b.getId(), b.getName(), b.getFranchiseId()));
    }

    // Plus: actualizar nombre sucursal
    @PutMapping("/branches/{branchId}")
    public Mono<BranchResponse> updateBranchName(@PathVariable String branchId,
                                                 @Valid @RequestBody BranchRequest request) {
        return franchiseService.updateBranchName(branchId, request.name())
                .map(b -> new BranchResponse(b.getId(), b.getName(), b.getFranchiseId()));
    }

    // 4. Agregar producto a sucursal
    @PostMapping("/branches/{branchId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ProductResponse> addProductToBranch(@PathVariable String branchId,
                                                    @Valid @RequestBody ProductRequest request) {
        return franchiseService.addProductToBranch(branchId, request.name(), request.stock())
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getStock(), p.getBranchId()));
    }

    // Plus: actualizar nombre producto
    @PutMapping("/products/{productId}")
    public Mono<ProductResponse> updateProductName(@PathVariable String productId,
                                                   @Valid @RequestBody ProductRequest request) {
        // aquí usamos el stock también si lo quieres actualizar
        return franchiseService.updateProductName(productId, request.name())
                .flatMap(p -> franchiseService.updateProductStock(p.getId(), request.stock()))
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getStock(), p.getBranchId()));
    }

    // 5. Eliminar producto de sucursal
    @DeleteMapping("/branches/{branchId}/products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProductFromBranch(@PathVariable String branchId,
                                              @PathVariable String productId) {
        return franchiseService.deleteProductFromBranch(branchId, productId);
    }

    // 6. Modificar stock
    @PatchMapping("/products/{productId}/stock")
    public Mono<ProductResponse> updateProductStock(@PathVariable String productId,
                                                    @RequestParam Integer stock) {
        return franchiseService.updateProductStock(productId, stock)
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getStock(), p.getBranchId()));
    }

    // 7. Top products por sucursal para una franquicia
    @GetMapping("/{franchiseId}/top-products-by-branch")
    public Flux<TopProductByBranchResponse> getTopProductsByBranch(@PathVariable String franchiseId) {
        return franchiseService.getTopProductsByBranchForFranchise(franchiseId)
                .map(p -> new TopProductByBranchResponse(
                        p.getId(),
                        p.getName(),
                        p.getStock(),
                        p.getBranchId()
                ));
    }
}
