package com.example.testassignmentapp.exchangerate;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.datatype.DatatypeConfigurationException;

@RestController
@RequestMapping("api/v1/exchange_rate")
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("update")
    public void updateExchangeRate() throws DatatypeConfigurationException, JsonProcessingException {
        this.exchangeRateService.updateExchangeRate();
    }
}
