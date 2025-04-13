package com.tredbase.shared_payment_processing.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long transactionId;
    private Long parentId;
    private Long studentId;
    private BigDecimal originalAmount;
    private BigDecimal adjustedAmount;
    private BigDecimal dynamicRate;
    private LocalDateTime transactionDate;
    private String message;
}
