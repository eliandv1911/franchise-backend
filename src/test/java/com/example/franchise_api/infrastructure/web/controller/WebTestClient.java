package com.example.franchise_api.infrastructure.web.controller;

import com.example.franchise_api.application.service.FranchiseService;
import com.example.franchise_api.domain.model.Franchise;
import com.example.franchise_api.infrastructure.web.dto.FranchiseRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = FranchiseController.class)
class FranchiseControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private FranchiseService franchiseService;

    @Test
    void createFranchise_shouldReturnCreated() {
        Franchise franchise = Franchise.builder().id("1").name("New Franchise").build();

        Mockito.when(franchiseService.createFranchise("New Franchise"))
                .thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/api/franchises")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new FranchiseRequest("New Franchise"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.name").isEqualTo("New Franchise");
    }
}
