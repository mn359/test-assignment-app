package com.example.testassignmentapp.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {

    private TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping()
    public void addTransaction(@RequestBody @Valid TransactionDTO dto) {
        this.transactionService.addTransaction(dto);
    }

    @GetMapping
    public void getTransactions(@RequestBody @Valid TransactionReportRequest request) throws JsonProcessingException {
        this.transactionService.getTransactions(request);
    }
}
