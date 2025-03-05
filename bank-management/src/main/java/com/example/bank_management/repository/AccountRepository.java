package com.example.bank_management.repository;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomer(Customer customer);
    Optional<Account> findById(long id);
    Optional<Account> findByName(String name);
    Optional<Account> findByCustomerAndName(Customer customer, String accountName);
}