package com.example.testassignmentapp.cbr;

import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.List;

public interface CbrWebService {
    List<ExchangeRateDTO> getCurrentExchangeRates();

    List<ExchangeRateDTO> getExchangeRatesForCurrencyInPeriod(LocalDate from,
                                                              LocalDate to,
                                                              String currencyCode,
                                                              String internalCbrCurrencyCode) throws JsonProcessingException;

    List<CbrCurrency> getDailyCurrencies() throws JsonProcessingException;
}
