package com.example.bank_management.repository;

import com.example.bank_management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccount_CustomerId(Long customerId);
}
