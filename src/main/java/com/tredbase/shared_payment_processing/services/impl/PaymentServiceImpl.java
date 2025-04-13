package com.tredbase.shared_payment_processing.services.impl;

import com.tredbase.shared_payment_processing.dtos.PaymentRequest;
import com.tredbase.shared_payment_processing.dtos.PaymentResponse;
import com.tredbase.shared_payment_processing.exceptions.ParentNotFoundException;
import com.tredbase.shared_payment_processing.exceptions.PaymentAuthorizationException;
import com.tredbase.shared_payment_processing.exceptions.StudentNotFoundException;
import com.tredbase.shared_payment_processing.models.entities.ParentEntity;
import com.tredbase.shared_payment_processing.models.entities.PaymentLedgerEntity;
import com.tredbase.shared_payment_processing.models.entities.StudentEntity;
import com.tredbase.shared_payment_processing.repositories.ParentRepository;
import com.tredbase.shared_payment_processing.repositories.PaymentLedgerRepository;
import com.tredbase.shared_payment_processing.repositories.StudentRepository;
import com.tredbase.shared_payment_processing.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Service implementation for processing payments between parents and students.
 * Handles both unique and shared student payments with atomic transaction support.
 */
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    // Repository dependencies injected via constructor (Lombok @RequiredArgsConstructor)
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final PaymentLedgerRepository paymentLedgerRepository;

    /**
     * Processes a payment transaction between parent and student.
     *
     * @Annotation @Transactional added for atomicity
     * @param request Payment request containing parentId, studentId and amount
     * @return PaymentResponse with transaction details
     * @throws ParentNotFoundException if specified parent doesn't exist
     * @throws StudentNotFoundException if specified student doesn't exist
     * @throws PaymentAuthorizationException if parent isn't authorized to pay for student
     */
    @Transactional
    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // Validate and fetch parent entity - throws exception if not found
        ParentEntity payingParent = parentRepository.findById(request.getParentId())
                .orElseThrow(() -> new ParentNotFoundException("Parent with " + request.getParentId() + " not found."));

        // Validate and fetch student entity - throws exception if not found
        StudentEntity student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student with " + request.getStudentId() + " not found."));

        // Verify payment authorization - parent must be associated with student
        if (!payingParent.getStudents().contains(student)) {
            throw new PaymentAuthorizationException(
                    "Parent is not authorized to pay for this student");
        }

        // Calculate dynamic rate based on business rules
        BigDecimal dynamicRate = calculateDynamicRate(payingParent, student);

        // Apply rate to get adjusted payment amount
        BigDecimal adjustedAmount = request.getAmount()
                .multiply(BigDecimal.ONE.add(dynamicRate));

        // Update student balance (credit)
        student.setBalance(student.getBalance().add(adjustedAmount));
        studentRepository.save(student);

        // Get all parents associated with student (for shared payments)
        List<ParentEntity> associatedParents = new ArrayList<>(student.getParents());

        // Calculate each parent's share of payment (equal division for shared students)
        BigDecimal amountPerParent = adjustedAmount.divide(
                new BigDecimal(associatedParents.size()),
                2, RoundingMode.HALF_UP);

        // Update each parent's balance (debit)
        for (ParentEntity parent : associatedParents) {
            parent.setBalance(parent.getBalance().subtract(amountPerParent));
            parentRepository.save(parent);
        }

        // Create audit record in payment ledger
        PaymentLedgerEntity ledger = new PaymentLedgerEntity(
                payingParent,
                student,
                request.getAmount(),
                adjustedAmount,
                String.format("Payment processed with dynamic rate %.2f%%",
                        dynamicRate.multiply(BigDecimal.valueOf(100)))
        );
        paymentLedgerRepository.save(ledger);

        // Return comprehensive payment response
        return new PaymentResponse(
                ledger.getId(),
                payingParent.getId(),
                student.getId(),
                request.getAmount(),
                adjustedAmount,
                dynamicRate,
                ledger.getTransactionDate(),
                "Payment processed successfully");
    }

    /**
     * Calculates dynamic rate based on business rules:
     * - Base rate: 5%
     * - Additional 2% for shared students
     * - 1% discount for parents with positive balance
     *
     * @param parent The parent making the payment
     * @param student The student being paid for
     * @return Calculated dynamic rate
     */
    private BigDecimal calculateDynamicRate(ParentEntity parent, StudentEntity student) {
        boolean isShared = student.getParents().size() > 1;
        BigDecimal baseRate = new BigDecimal("0.05"); // Standard 5% fee

        // Apply additional 2% for shared students
        if (isShared) {
            baseRate = baseRate.add(new BigDecimal("0.02"));
        }

        // Apply 1% discount for parents with positive balance
        if (parent.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            baseRate = baseRate.subtract(new BigDecimal("0.01"));
        }

        return baseRate;
    }
}