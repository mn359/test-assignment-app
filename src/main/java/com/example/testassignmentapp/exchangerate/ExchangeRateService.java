package com.example.testassignmentapp.exchangerate;

import com.example.testassignmentapp.common.DateTimeUtils;
import com.example.testassignmentapp.exchangerate.currency.Currency;
import com.example.testassignmentapp.exchangerate.currency.CurrencyService;
import com.example.testassignmentapp.cbr.CbrWebService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final CbrWebService cbrWebService;
    private final ExchangeRateRepository exchangeRateRepository;

    private final CurrencyService currencyService;

    @Autowired
    public ExchangeRateService(@Qualifier("http") CbrWebService cbrWebService,
                               ExchangeRateRepository exchangeRateRepository,
                               CurrencyService currencyService) {
        this.cbrWebService = cbrWebService;
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyService = currencyService;
    }

    public void updateExchangeRate() throws JsonProcessingException {
        List<ExchangeRateDTO> data = cbrWebService.getCurrentExchangeRates();

        Map<String, Currency> currencyByCode = currencyService.getAllCurrenciesByCode();

        var exchangeRates = data.stream()
                .filter(o -> currencyByCode.containsKey(o.currency()))
                .map(o -> new ExchangeRate(
                        currencyByCode.get(o.currency()),
                        DateTimeUtils.now(),
                        new BigDecimal(o.rate())
                ) )
                .toList();

        exchangeRateRepository.saveAll(exchangeRates);
    }
}