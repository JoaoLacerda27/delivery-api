package com.company.delivery_api.application.delivery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to create a new delivery")
public record CreateDeliveryRequest(
        @NotBlank(message = "Street is mandatory")
        @Schema(description = "Street address", example = "Main Street")
        String street,

        @NotBlank(message = "City is mandatory")
        @Schema(description = "City name", example = "São Paulo")
        String city,

        @NotBlank(message = "State is mandatory")
        @Schema(description = "State abbreviation", example = "SP")
        String state,

        @NotBlank(message = "Zip code is mandatory")
        @Schema(description = "ZIP/CEP code", example = "01001000")
        String zipCode
) {}

