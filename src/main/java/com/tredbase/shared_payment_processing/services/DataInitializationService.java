package com.tredbase.shared_payment_processing.services;

import com.tredbase.shared_payment_processing.models.entities.ParentEntity;
import com.tredbase.shared_payment_processing.models.entities.StudentEntity;
import com.tredbase.shared_payment_processing.models.entities.UserEntity;
import com.tredbase.shared_payment_processing.repositories.ParentRepository;
import com.tredbase.shared_payment_processing.repositories.PaymentLedgerRepository;
import com.tredbase.shared_payment_processing.repositories.StudentRepository;
import com.tredbase.shared_payment_processing.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInitializationService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final PaymentLedgerRepository paymentLedgerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public void initializeDatabase() {
        // Clear existing data (order matters due to foreign key constraints)
        paymentLedgerRepository.deleteAll();
        parentRepository.deleteAll();
        studentRepository.deleteAll();

        // Create parents
        ParentEntity parentA = new ParentEntity("Parent A", new BigDecimal("100000.00"));
        ParentEntity parentB = new ParentEntity("Parent B", new BigDecimal("150000.00"));

        parentA = parentRepository.save(parentA);
        parentB = parentRepository.save(parentB);

        // Create students
        StudentEntity student1 = new StudentEntity("Student 1", BigDecimal.ZERO); // Shared student
        StudentEntity student2 = new StudentEntity("Student 2", BigDecimal.ZERO);
        StudentEntity student3 = new StudentEntity("Student 3", BigDecimal.ZERO);

        student1 = studentRepository.save(student1);
        student2 = studentRepository.save(student2);
        student3 = studentRepository.save(student3);

        // Establish relationships
        parentA.addStudent(student1);
        parentB.addStudent(student1);
        parentA.addStudent(student2);
        parentB.addStudent(student3);

        // Save the updated parents to persist relationships
        parentRepository.saveAll(List.of(parentA, parentB));

        //Create admin login details
        createAdmin();
    }

    private void createAdmin(){
        UserEntity adminUser = new UserEntity("admin@testemail.com",encoder.encode("1234"));
        userRepository.save(adminUser);
    }
}
