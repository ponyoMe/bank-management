package com.example.bank_management.controller;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import com.example.bank_management.service.AccountService;
import com.example.bank_management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.example.bank_management.model.Account;
import com.example.bank_management.model.Customer;
import com.example.bank_management.service.AccountService;
import com.example.bank_management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    public String showDashboard(Model model, Principal principal) {

        return "dashboard";
    }
}


