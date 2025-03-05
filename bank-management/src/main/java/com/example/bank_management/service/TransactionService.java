package com.example.bank_management.service;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import com.example.bank_management.repository.AccountRepository;
import com.example.bank_management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public void transferBetweenOwnAccounts(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (fromAccount.getCustomer().getId().equals(toAccount.getCustomer().getId())) {
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient balance");
            }

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
        } else {
            throw new RuntimeException("Can only transfer between own accounts!");
        }
    }

    @Transactional
    public void transferToExternalUser(Long fromAccountId, String toEmail, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Optional<Customer> recipientOpt = customerRepository.findByEmail(toEmail);
        if (recipientOpt.isEmpty()) {
            throw new RuntimeException("Recipient email not found!");
        }

        Customer recipient = recipientOpt.get();
        Account mainAccount = accountRepository.findByCustomerAndName(recipient, "main")
                .orElseThrow(() -> new RuntimeException("Recipient does not have a 'main' account!"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        mainAccount.setBalance(mainAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(mainAccount);
    }

}
