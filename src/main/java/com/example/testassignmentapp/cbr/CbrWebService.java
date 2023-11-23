package com.example.testassignmentapp.cbr;

import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.List;

public interface CbrWebService {
    List<ExchangeRateDTO> getCurrentExchangeRates() throws JsonProcessingException;
    void getDynamicExchangeRate(LocalDate from, LocalDate to, String internalCurrencyCode) throws JsonProcessingException;
    void getDailyCurrencies() throws JsonProcessingException;
}
