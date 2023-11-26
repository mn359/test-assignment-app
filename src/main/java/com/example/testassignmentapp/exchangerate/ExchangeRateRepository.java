package com.example.testassignmentapp.exchangerate;

import com.example.testassignmentapp.currency.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
    List<ExchangeRate> findByDatetimeBetweenAndCurrency(LocalDateTime from, LocalDateTime to, Currency currency);
}
