package com.example.bank_management.service;


import com.example.bank_management.model.Account;
import com.example.bank_management.model.AccountType;
import com.example.bank_management.model.Customer;
import com.example.bank_management.repository.AccountRepository;
import com.example.bank_management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public List<Account> getAccountsByCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Customer> customer = customerRepository.findByEmail(username);
        return accountRepository.findByCustomer(customer.orElse(null));
    }

    public Account getAccountById(int accountId) {
        Optional<Account> optionalAccount = accountRepository.findById(accountId);

        if (optionalAccount.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }

        Account account = optionalAccount.get();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Customer> customer = customerRepository.findByEmail(username);

        if (customer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        if (!account.getCustomer().equals(customer.get())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this account");
        }

        return account;
    }

    public void createCheckingAccount(String accountName) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Customer> customerOpt = customerRepository.findByEmail(username);

        if (customerOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }

        Customer customer = customerOpt.get();

        if (accountRepository.findByName(accountName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account with this name already exists");
        }

        Account newAccount = new Account();
        newAccount.setAccountType("Checking");
        newAccount.setName(accountName);
        newAccount.setBalance(new BigDecimal(0));
        newAccount.setCreationDate(LocalDateTime.now());
        newAccount.setLockedUntil(LocalDateTime.now());
        newAccount.setCustomer(customer);

        accountRepository.save(newAccount); // Save to DB and return
    }

    public void createSavingAccount(String accountName, int lockInPeriod) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Customer> customerOpt = customerRepository.findByEmail(username);

        if (customerOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
        }


        if (accountRepository.findByName(accountName).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account with this name already exists");
        }


        Customer customer = customerOpt.get();

        Account newAccount = new Account();
        newAccount.setAccountType("Saving");
        newAccount.setName(accountName);
        newAccount.setBalance(new BigDecimal(0));
        newAccount.setCreationDate(LocalDateTime.now());
        newAccount.setLockedUntil(LocalDateTime.now().plusMonths(lockInPeriod));
        newAccount.setCustomer(customer);

        accountRepository.save(newAccount);
    }

}

