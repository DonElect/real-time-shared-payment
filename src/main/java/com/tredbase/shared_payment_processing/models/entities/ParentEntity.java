package com.tredbase.shared_payment_processing.models.entities;

import com.tredbase.shared_payment_processing.models.enums.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_parents",
        indexes = {@Index(name = "idx_parent_name", columnList = "name")})
public class ParentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tbl_parent_student",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<StudentEntity> students = new HashSet<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PaymentLedgerEntity> payments = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Roles role = Roles.PARENT;

    public ParentEntity(String name, BigDecimal balance) {
        this.name = name;
        this.balance = balance;
    }

    public void addStudent(StudentEntity student) {
        this.students.add(student);
        student.getParents().add(this);
    }

    public void removeStudent(StudentEntity student) {
        this.students.remove(student);
        student.getParents().remove(this);
    }
}
