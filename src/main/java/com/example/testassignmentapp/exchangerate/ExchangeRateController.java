package com.example.testassignmentapp.exchangerate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/exchange_rate")
public class ExchangeRateController {

    private ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @PostMapping("update")
    public void updateExchangeRate(@RequestBody(required = false) ExchangeRateUpdateRequest requestBody) {
        if (requestBody == null) {
            this.exchangeRateService.updateExchangeRate();
        } else {
            this.exchangeRateService.updateExchangeRate(requestBody.date());
        }
    }
}

