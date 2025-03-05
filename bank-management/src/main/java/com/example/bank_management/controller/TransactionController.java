package com.example.bank_management.controller;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import com.example.bank_management.model.Transaction;
import com.example.bank_management.repository.AccountRepository;
import com.example.bank_management.repository.CustomerRepository;
import com.example.bank_management.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public String showTransactionForm(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Customer> customerOpt = customerRepository.findByEmail(username);

        if (customerOpt.isEmpty()) {
            model.addAttribute("error", "User not found");
            return "transaction_index"; // Reload form with error
        }

        Customer customer = customerOpt.get();
        List<Account> accounts = accountRepository.findByCustomer(customer);

        model.addAttribute("accounts", accounts); // User's accounts
        model.addAttribute("transaction", new Transaction()); // Empty transaction object
        return "transaction_index";
    }


    @PostMapping("/create_internal_transaction")
    public String createInternalTransaction(@RequestParam Long fromAccountId,
                                            @RequestParam Long toAccountId,
                                            @RequestParam BigDecimal amount,
                                            RedirectAttributes redirectAttributes) {
        try {
            transactionService.transferBetweenOwnAccounts(fromAccountId, toAccountId, amount);
            redirectAttributes.addFlashAttribute("success", "Internal transfer successful!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transaction/";
    }


    @PostMapping("/create_external_transaction")
    public String createExternalTransaction(@RequestParam Long fromAccountId,
                                            @RequestParam String toEmail,
                                            @RequestParam BigDecimal amount,
                                            RedirectAttributes redirectAttributes) {
        try {
            transactionService.transferToExternalUser(fromAccountId, toEmail, amount);
            redirectAttributes.addFlashAttribute("success", "Money sent successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/transaction/";
    }

}
