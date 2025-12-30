package com.company.delivery_api.application.integrations.viacep.client;

import com.company.delivery_api.application.integrations.viacep.dto.ViaCepResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ViaCepClient {

    private final WebClient webClient;

    public ViaCepClient(
            WebClient.Builder builder,
            @Value("${integrations.via-cep.url}") String baseUrl
    ) {
        this.webClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public ViaCepResponse getAddress(String cep) {
        try {
            return webClient
                    .get()
                    .uri("/{cep}/json", cep.replaceAll("[^0-9]", ""))
                    .retrieve()
                    .bodyToMono(ViaCepResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Failed to fetch address from ViaCEP for CEP: " + cep, e);
        }
    }
}

