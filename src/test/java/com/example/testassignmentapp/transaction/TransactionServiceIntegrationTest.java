package com.example.testassignmentapp.transaction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TransactionServiceIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void testAddTransaction() {
        TransactionDTO dto = new TransactionDTO(LocalDate.of(2022, 2, 4), "tr1", BigDecimal.valueOf(1.1));

        TransactionService transactionService = new TransactionService(
                transactionRepository, null, null);
        transactionService.addTransaction(dto);

        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions).hasSize(1);

        Transaction transaction = transactions.get(0);
        assertThat(transaction.getDate()).isEqualTo(dto.date());
        assertThat(transaction.getDescription()).isEqualTo(dto.description());
        assertThat(transaction.getValue()).isEqualTo(dto.value());
    }
}
