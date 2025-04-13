package com.tredbase.shared_payment_processing.repositories;

import com.tredbase.shared_payment_processing.models.entities.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long> {
}