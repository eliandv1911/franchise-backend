package com.example.franchise_api.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch {
    private String id;
    private String name;
    private String franchiseId;
}
