package com.example.testassignmentapp.exchangerate;

import com.example.testassignmentapp.exchangerate.web.ExchangeRateWebService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;

@Service
public class ExchangeRateService {

    private final ExchangeRateWebService exchangeRateWebService;

    @Autowired
    public ExchangeRateService(@Qualifier("http") ExchangeRateWebService exchangeRateWebService) {
        this.exchangeRateWebService = exchangeRateWebService;
    }

    public void updateExchangeRate() throws JsonProcessingException {
        exchangeRateWebService.getCurrentExchangeRates();
    }
}