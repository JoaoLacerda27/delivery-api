package com.company.delivery_api.application.integrations.viacep.controller;

import com.company.delivery_api.application.integrations.viacep.dto.AddressResponse;
import com.company.delivery_api.application.integrations.viacep.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Addresses", description = "Address lookup API using ViaCEP integration")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{cep}")
    @Operation(
            summary = "Get address by CEP",
            description = "Retrieves address information for a given CEP (ZIP code) using ViaCEP API. Results are cached in MongoDB."
    )
    public ResponseEntity<AddressResponse> getAddressByCep(
            @Parameter(description = "CEP (ZIP code) - can be formatted with or without dashes", example = "06824440")
            @PathVariable String cep
    ) {
        AddressResponse response = addressService.getAddressByCep(cep);
        return ResponseEntity.ok(response);
    }
}


