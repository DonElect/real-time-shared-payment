package com.tredbase.shared_payment_processing.models.entities;

import com.tredbase.shared_payment_processing.models.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_students",
        indexes = {@Index(name = "idx_student_name", columnList = "name")})
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @NotBlank(message = "Student name is required")
    @Size(max = 100, message = "Student name must be less than 100 characters")
    private String name;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Balance cannot be null")
    @Digits(integer = 17, fraction = 2, message = "Balance must have up to 17 integer and 2 fraction digits")
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ParentEntity> parents = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<PaymentLedgerEntity> payments = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Roles role = Roles.STUDENT;

    public StudentEntity(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }
}
