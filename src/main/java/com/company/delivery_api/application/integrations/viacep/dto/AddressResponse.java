package com.company.delivery_api.application.integrations.viacep.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Address information from ViaCEP")
public record AddressResponse(
        @Schema(description = "CEP (ZIP code)", example = "06824440")
        String cep,
        
        @Schema(description = "Street name", example = "Rua das Flores")
        String street,
        
        @Schema(description = "Neighborhood", example = "Jardim das Acácias")
        String neighborhood,
        
        @Schema(description = "City name", example = "Embu das Artes")
        String city,
        
        @Schema(description = "State abbreviation", example = "SP")
        String state,
        
        @Schema(description = "Complement (if available)", example = "Apto 101")
        String complement
) {}


