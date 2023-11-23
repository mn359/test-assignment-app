package com.example.testassignmentapp.exchangerate;

import com.example.testassignmentapp.exchangerate.currency.Currency;
import com.example.testassignmentapp.exchangerate.currency.CurrencyService;
import com.example.testassignmentapp.cbr.CbrWebService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Map<String, Currency> codeToCurrencyMap = currencyService
                .findCurrenciesByCodes("EUR", "USD")
                .stream().collect(Collectors.toMap(Currency::getCode, o -> o));

        List<ExchangeRateDTO> data = cbrWebService.getCurrentExchangeRates();

        var exchangeRates = data.stream()
                .filter(dto -> codeToCurrencyMap.containsKey(dto.currency()))
                .map(dto -> this.createExchangeRate(codeToCurrencyMap.get(dto.currency()), dto))
                .toList();

        exchangeRateRepository.saveAll(exchangeRates);
    }

    public List<ExchangeRate> getExchangeRatesForCurrencyInPeriod(LocalDate from, LocalDate to, Currency currency) throws JsonProcessingException {
        return cbrWebService
                .getExchangeRatesForCurrencyInPeriod(
                        from,
                        to,
                        currency.getCode(),
                        currency.getInternalCbrCode())
                .stream()
                .map(dto -> createExchangeRate(currency, dto))
                .toList();
    }

    public List<ExchangeRate> createExchangeRates(Map<String, Currency> codeToCurrencyMap, List<ExchangeRateDTO> dtos) {
        return dtos.stream()
                .filter(dto -> codeToCurrencyMap.containsKey(dto.currency()))
                .map(dto -> createExchangeRate(codeToCurrencyMap.get(dto.currency()), dto))
                .toList();
    }

    public ExchangeRate createExchangeRate(Currency currency, ExchangeRateDTO dto) {
        ExchangeRate exchangeRate = new ExchangeRate(currency,dto.dateTime(), new BigDecimal(dto.rate()));
        return exchangeRate;
    }

    public ExchangeRate createExchangeRate(ExchangeRateDTO dto) {

        Currency currency = currencyService.getCurrencyByCode(dto.currency());
        if (currency == null) {
            throw new EntityNotFoundException("Currency not found for code: " + dto.currency());
        }
        ExchangeRate exchangeRate = new ExchangeRate(currency,dto.dateTime(), new BigDecimal(dto.rate()));
        return exchangeRate;
    }
}