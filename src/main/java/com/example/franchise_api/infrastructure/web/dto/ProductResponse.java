package com.example.franchise_api.infrastructure.web.dto;

public record ProductResponse(
        String id,
        String name,
        Integer stock,
        String branchId
) {}
