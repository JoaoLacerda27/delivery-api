package com.company.delivery_api.application.integrations.viacep.service;

import com.company.delivery_api.application.delivery.domain.mongo.AddressInfo;
import com.company.delivery_api.application.delivery.repository.mongo.AddressInfoRepository;
import com.company.delivery_api.application.integrations.viacep.client.ViaCepClient;
import com.company.delivery_api.application.integrations.viacep.dto.AddressResponse;
import com.company.delivery_api.application.integrations.viacep.dto.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final ViaCepClient viaCepClient;
    private final AddressInfoRepository addressInfoRepository;

    public AddressResponse getAddressByCep(String cep) {
        String normalizedCep = cep.replaceAll("[^0-9]", "");
        
        AddressInfo cached = addressInfoRepository.findByCep(normalizedCep)
                .orElse(null);
        
        if (cached != null) {
            return toResponse(cached);
        }
        
        ViaCepResponse viaCepResponse = viaCepClient.getAddress(normalizedCep);
        
        if (Boolean.TRUE.equals(viaCepResponse.erro()) || 
            viaCepResponse.cep() == null || 
            viaCepResponse.cep().isEmpty()) {
            throw new IllegalArgumentException("CEP não encontrado: " + cep);
        }
        
        AddressInfo addressInfo = AddressInfo.builder()
                .cep(normalizedCep)
                .street(viaCepResponse.logradouro() != null ? viaCepResponse.logradouro() : "")
                .neighborhood(viaCepResponse.bairro() != null ? viaCepResponse.bairro() : "")
                .city(viaCepResponse.localidade() != null ? viaCepResponse.localidade() : "")
                .state(viaCepResponse.uf() != null ? viaCepResponse.uf() : "")
                .fetchedAt(Instant.now())
                .build();
        
        addressInfoRepository.save(addressInfo);
        
        return toResponse(addressInfo, viaCepResponse.complemento());
    }
    
    private AddressResponse toResponse(AddressInfo addressInfo) {
        return new AddressResponse(
                addressInfo.getCep(),
                addressInfo.getStreet(),
                addressInfo.getNeighborhood(),
                addressInfo.getCity(),
                addressInfo.getState(),
                null
        );
    }
    
    private AddressResponse toResponse(AddressInfo addressInfo, String complement) {
        return new AddressResponse(
                addressInfo.getCep(),
                addressInfo.getStreet(),
                addressInfo.getNeighborhood(),
                addressInfo.getCity(),
                addressInfo.getState(),
                complement
        );
    }
}

