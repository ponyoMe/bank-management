package com.example.bank_management.controller;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import com.example.bank_management.repository.CustomerRepository;
import com.example.bank_management.service.AccountService;
import com.example.bank_management.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/my_accounts")
public class MyAccountsController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public String myAccounts(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Customer> customer = customerRepository.findByEmail(username);
        model.addAttribute("account_list", accountService.getAccountsByCustomer());
        model.addAttribute("customer", customer.get());
        return "my_accounts_index";
    }

    @GetMapping("/{id}")
    public String myAccounts(@PathVariable int id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Customer> customer = customerRepository.findByEmail(username);
        model.addAttribute("customer", customer.get());
        model.addAttribute("account", accountService.getAccountById(id));
        model.addAttribute("transactions", transactionService.findByUser(accountService.getAccountById(id)));
        return "my_accounts_details";
    }

    @GetMapping("/create_saving_account")
    public String createSavingAccountPage(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Customer> customer = customerRepository.findByEmail(username);
        model.addAttribute("customer", customer.get());
        model.addAttribute("account_name", "");
        model.addAttribute("lock_in_period", "");
        return "create_saving_account";
    }
    @PostMapping("/create_saving_account")
    public String createSavingAccount(@RequestParam String account_name,
                                        @RequestParam int lock_in_period,
                                        RedirectAttributes redirectAttributes) {
        try {
            accountService.createSavingAccount(account_name, lock_in_period);
            redirectAttributes.addFlashAttribute("success", "Saving account created successfully!");
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
        }

        return "redirect:/my_accounts/";
    }

    @GetMapping("/create_checking_account")
    public String createCheckingAccountPage(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Customer> customer = customerRepository.findByEmail(username);
        model.addAttribute("customer", customer.get());
        model.addAttribute("account_name", "");
        return "create_checking_account";
    }
    @PostMapping("/create_checking_account")
    public String createCheckingAccount(@RequestParam String account_name,
                                        RedirectAttributes redirectAttributes) {
        try {
            accountService.createCheckingAccount(account_name);
            redirectAttributes.addFlashAttribute("success", "Checking account created successfully!");
        } catch (ResponseStatusException e) {
            redirectAttributes.addFlashAttribute("error", e.getReason());
        }

        return "redirect:/my_accounts/";
    }


}
