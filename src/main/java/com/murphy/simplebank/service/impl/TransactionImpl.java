package com.murphy.simplebank.service.impl;

import com.murphy.simplebank.dto.TransactionDto;
import com.murphy.simplebank.entity.Transaction;
import com.murphy.simplebank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionImpl implements TransactionService{
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")

                .build();
        transactionRepository.save(transaction);
        System.out.println("Transaction saved successfully");
    }
}
