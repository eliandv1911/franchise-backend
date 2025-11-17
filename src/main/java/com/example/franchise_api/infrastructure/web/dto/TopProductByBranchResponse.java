package com.example.franchise_api.infrastructure.web.dto;

public record TopProductByBranchResponse(
        String productId,
        String productName,
        Integer stock,
        String branchId
) {}
