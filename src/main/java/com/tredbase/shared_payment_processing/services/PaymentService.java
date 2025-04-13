package com.tredbase.shared_payment_processing.services;

import com.tredbase.shared_payment_processing.dtos.PaymentRequest;
import com.tredbase.shared_payment_processing.dtos.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
}
