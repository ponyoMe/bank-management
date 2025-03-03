package com.example.bank_management.controller;

import com.example.bank_management.model.Customer;
import com.example.bank_management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new Customer());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") Customer user, BindingResult result, Model model) {
        if (customerService.findByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "An account with this email already exists!");
        }

        if (result.hasErrors()) {
            return "registration";
        }

        customerService.registerCustomer(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // No redirects here
    }
}

