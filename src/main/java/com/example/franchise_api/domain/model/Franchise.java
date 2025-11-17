package com.example.franchise_api.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Franchise {
    private String id;
    private String name;
    private List<Branch> branches;
}
