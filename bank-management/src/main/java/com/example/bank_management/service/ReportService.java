package com.example.bank_management.service;

import com.example.bank_management.model.Transaction;
import com.example.bank_management.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private TransactionRepository transactionRepository;

    public ReportService(TransactionRepository transactionRepository){
        this.transactionRepository=transactionRepository;
    }

    public List<Transaction> getTransactionForCustomer(Long id){
        return transactionRepository.findByFromAccount_CustomerId(id);
    }
}
