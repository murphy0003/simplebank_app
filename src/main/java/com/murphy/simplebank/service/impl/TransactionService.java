package com.murphy.simplebank.service.impl;

import com.murphy.simplebank.dto.TransactionDto;
import com.murphy.simplebank.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
