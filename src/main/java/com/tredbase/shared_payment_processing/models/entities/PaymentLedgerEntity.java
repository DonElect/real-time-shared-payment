package com.tredbase.shared_payment_processing.models.entities;

import com.tredbase.shared_payment_processing.models.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_payment_ledger",
        indexes = {
                @Index(name = "idx_ledger_parent", columnList = "parent_id"),
                @Index(name = "idx_ledger_student", columnList = "student_id"),
                @Index(name = "idx_ledger_date", columnList = "transaction_date")
        })
public class PaymentLedgerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private ParentEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "adjusted_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal adjustedAmount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.COMPLETED;

    public PaymentLedgerEntity(ParentEntity parent, StudentEntity student, BigDecimal amount,
                         BigDecimal adjustedAmount, String description) {
        this.parent = parent;
        this.student = student;
        this.amount = amount;
        this.adjustedAmount = adjustedAmount;
        this.transactionDate = LocalDateTime.now();
        this.description = description;
    }
}

