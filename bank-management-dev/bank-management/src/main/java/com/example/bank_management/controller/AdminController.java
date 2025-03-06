package com.example.bank_management.controller;


import ch.qos.logback.core.model.Model;
import com.example.bank_management.model.Customer;
import com.example.bank_management.repository.CustomerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private CustomerRepository customerRepository;

    public AdminController(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model){
        List<Customer> customers = customerRepository.findAll();
        return "admin-dashboard";

    }
}
