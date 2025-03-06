package com.example.bank_management.repository;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.fromAccount = :account OR t.toAccount = :account")
    List<Transaction> findByAccount(@Param("account") Account account);

    List<Transaction> findByFromAccount_CustomerId(Long id);
}
