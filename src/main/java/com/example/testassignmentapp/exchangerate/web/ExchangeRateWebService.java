package com.example.testassignmentapp.exchangerate.web;

import com.example.testassignmentapp.exchangerate.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ExchangeRateWebService {
    List<ExchangeRateDTO> getCurrentExchangeRates() throws JsonProcessingException;
}
