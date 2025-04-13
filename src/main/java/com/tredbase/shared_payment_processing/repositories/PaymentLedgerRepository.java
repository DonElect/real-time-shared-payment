package com.tredbase.shared_payment_processing.repositories;

import com.tredbase.shared_payment_processing.models.entities.PaymentLedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentLedgerRepository extends JpaRepository<PaymentLedgerEntity, Long> {
}