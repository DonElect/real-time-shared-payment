package com.tredbase.shared_payment_processing.controllers;

import com.tredbase.shared_payment_processing.dtos.PaymentRequest;
import com.tredbase.shared_payment_processing.exceptions.ParentNotFoundException;
import com.tredbase.shared_payment_processing.exceptions.PaymentAuthorizationException;
import com.tredbase.shared_payment_processing.exceptions.StudentNotFoundException;
import com.tredbase.shared_payment_processing.models.ApiResponse;
import com.tredbase.shared_payment_processing.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling payment processing operations.
 * Provides endpoints for managing parent-to-student payments.
 */
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    // Injected payment service for business logic
    private final PaymentService paymentService;

    /**
     * Processes a payment transaction between parent and student.
     *
     * @param request Payment details including:
     *                - parentId: ID of paying parent
     *                - studentId: ID of beneficiary student
     *                - amount: Payment amount
     * @return ResponseEntity containing:
     *         - Standard API response wrapper
     *         - HTTP 200 status on success
     *         - Payment details in data field
     * @throws ParentNotFoundException if parent doesn't exist
     * @throws StudentNotFoundException if student doesn't exist
     * @throws PaymentAuthorizationException if parent isn't authorized for student
     */
    @PostMapping
    public ResponseEntity<ApiResponse> processPayment(@RequestBody PaymentRequest request) {
        // Process payment through service layer and wrap response
        return ResponseEntity.ok(
                new ApiResponse(
                        "200",
                        "Payment processed successfully",
                        paymentService.processPayment(request)
                )
        );
    }
}