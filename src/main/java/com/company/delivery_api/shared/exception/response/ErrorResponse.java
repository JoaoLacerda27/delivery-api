package com.company.delivery_api.shared.exception.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ErrorResponse {
    private String errorCode;
    private String message;
}
