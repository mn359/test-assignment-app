package com.example.testassignmentapp.exchangerate.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public Map<String, Currency> getAllCurrenciesByCode() {
        return currencyRepository.findAll().stream().collect(Collectors.toMap(
                o -> o.getCode().name(),
                o->o
        ));
    }
}
