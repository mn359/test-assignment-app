package com.example.testassignmentapp.exchangerate.currency;

import com.example.testassignmentapp.cbr.CbrCurrency;
import com.example.testassignmentapp.cbr.CbrWebService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CbrWebService cbrWebService;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository,
                           @Qualifier("http") CbrWebService cbrWebService) {
        this.currencyRepository = currencyRepository;
        this.cbrWebService = cbrWebService;
    }

    public Map<String, Currency> getAllCurrenciesByCode() {
        return currencyRepository.findAll().stream().collect(Collectors.toMap(
                o -> o.getCode(),
                o->o
        ));
    }

    public Currency getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    public List<Currency> getCurrenciesFromCbrAndSave() throws JsonProcessingException {
        var data = cbrWebService.getDailyCurrencies();

        List<Currency> currencies = data
                .stream()
                .collect(Collectors.toMap(
                        CbrCurrency::code, // key extractor
                        Function.identity(),  // value mapper
                        (existing, replacement) -> existing)) // merge function to handle duplicate keys
                .values()
                .stream()
                .map(cbrc -> new Currency(cbrc.code(), cbrc.internalCbrCode()))
                .toList();
        currencyRepository.saveAll(currencies);

        return currencies;
    }

    public List<Currency> findCurrenciesByCodes(String... codes) {
        return currencyRepository.findByCodeIn(Arrays.asList(codes));
    }
}
