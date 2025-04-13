package com.tredbase.shared_payment_processing.models;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    private String message;
    private HttpStatus status;
    private String dateTime;
}
