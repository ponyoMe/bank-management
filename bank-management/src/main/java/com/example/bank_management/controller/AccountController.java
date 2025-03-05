package com.example.bank_management.controller;


import com.example.bank_management.model.Account;
import com.example.bank_management.model.AccountType;
import com.example.bank_management.model.Customer;
import com.example.bank_management.service.AccountService;
import com.example.bank_management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/my_accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String myAccounts(org.springframework.ui.Model model){
        model.addAttribute("account_list", accountService.getAccountsByCustomer());
        return "my_accounts_index";
    }

    @GetMapping("/{id}")
    public String myAccounts(@PathVariable int id, org.springframework.ui.Model model) {
        model.addAttribute("account", accountService.getAccountById(id));
        return "my_accounts_details";
    }

    @GetMapping("/create_saving_account")
    public String createSavingAccountPage(org.springframework.ui.Model model){
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
