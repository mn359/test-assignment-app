package com.example.testassignmentapp.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void addTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction(
                LocalDate.parse(dto.date()),
                dto.description(),
                new BigDecimal(dto.value())
        );

        transactionRepository.save(transaction);
    }
}
