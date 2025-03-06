package com.example.bank_management.service;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import com.example.bank_management.model.Transaction;
import com.example.bank_management.repository.AccountRepository;
import com.example.bank_management.repository.CustomerRepository;
import com.example.bank_management.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> findByUser(Account account) {
        return transactionRepository.findByAccount(account);
    }

    @Transactional
    public void transferBetweenOwnAccounts(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (!fromAccount.getCustomer().getId().equals(toAccount.getCustomer().getId())) {
            throw new RuntimeException("Can only transfer between own accounts!");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Save transaction record
        Transaction transaction = new Transaction(fromAccount, toAccount, amount);
        transactionRepository.save(transaction);
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

        // Perform transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        mainAccount.setBalance(mainAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(mainAccount);

        // Save transaction record
        Transaction transaction = new Transaction(fromAccount, mainAccount, amount);
        transactionRepository.save(transaction);
    }
}
