package com.tredbase.shared_payment_processing.exceptions;

public class PaymentAuthorizationException extends RuntimeException {
    public PaymentAuthorizationException(String message) {
        super(message);
    }
}
