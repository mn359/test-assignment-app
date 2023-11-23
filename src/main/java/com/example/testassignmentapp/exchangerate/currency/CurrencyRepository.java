package com.example.testassignmentapp.exchangerate.currency;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Integer> {
     Currency findByCode(String code);
     List<Currency> findByCodeIn(List<String> codes);
}
