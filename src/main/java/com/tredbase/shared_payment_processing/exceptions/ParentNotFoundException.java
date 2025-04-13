package com.tredbase.shared_payment_processing.exceptions;

public class ParentNotFoundException extends RuntimeException {
    public ParentNotFoundException(String message) {
        super(message);
    }
}
