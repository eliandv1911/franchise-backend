package com.example.franchise_api.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record BranchRequest(
        @NotBlank String name
) {}
