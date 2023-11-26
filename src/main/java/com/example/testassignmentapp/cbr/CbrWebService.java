package com.example.testassignmentapp.cbr;

import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CbrWebService {
    List<ExchangeRateDTO> getCurrentExchangeRates();

    List<ExchangeRateDTO> getExchangeRatesOnDate(LocalDateTime datetime);

    List<ExchangeRateDTO> getExchangeRatesForCurrencyInPeriod(LocalDate from,
                                                              LocalDate to,
                                                              String currencyCode,
                                                              String internalCbrCurrencyCode) ;

    List<CbrCurrency> getDailyCurrencies() ;
}
