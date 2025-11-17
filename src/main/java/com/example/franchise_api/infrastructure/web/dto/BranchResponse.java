package com.example.franchise_api.infrastructure.web.dto;

public record BranchResponse(
        String id,
        String name,
        String franchiseId
) {}
