package com.example.bank_management.service;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import com.example.bank_management.model.Role;
import com.example.bank_management.model.RoleType;
import com.example.bank_management.repository.AccountRepository;
import com.example.bank_management.repository.CustomerRepository;
import com.example.bank_management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomerService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    public void registerCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        // Assign default ROLE_USER
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        customer.getRoles().add(userRole);
        customerRepository.save(customer);


        Account account = new Account();
        account.setName("main");
        account.setAccountType("Checking");
        account.setCustomer(customer);
        account.setCreationDate(LocalDateTime.now());
        account.setLockedUntil(LocalDateTime.now());
        account.setBalance(new BigDecimal(0));
        accountRepository.save(account);

    }

    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> authorities = customer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new User(customer.getEmail(), customer.getPassword(), authorities);
    }


    public boolean emailExists(String email) {
        return customerRepository.findByEmail(email).isPresent(); // ✅ Returns true if user exists
    }


}


