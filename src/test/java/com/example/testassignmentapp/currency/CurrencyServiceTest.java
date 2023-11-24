package com.example.testassignmentapp.currency;

import com.example.testassignmentapp.cbr.CbrCurrency;
import com.example.testassignmentapp.cbr.CbrWebService;
import com.example.testassignmentapp.exchangerate.currency.Currency;
import com.example.testassignmentapp.exchangerate.currency.CurrencyRepository;
import com.example.testassignmentapp.exchangerate.currency.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private CbrWebService cbrWebService;

    @InjectMocks
    private CurrencyService currencyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCurrenciesFromCbrAndSave() {
        List<CbrCurrency> data = Arrays.asList(
                new CbrCurrency("EUR", "978"),
                new CbrCurrency("USD", "840")
        );

        List<Currency> expectedCurrencies = Arrays.asList(
                new Currency("EUR", "978"),
                new Currency("USD", "840")
        );

        when(cbrWebService.getDailyCurrencies()).thenReturn(data);

        List<Currency> result = currencyService.getCurrenciesFromCbrAndSave();

        verify(currencyRepository, times(1)).saveAll(expectedCurrencies);

        assertThat(expectedCurrencies).containsAll(result);
        assertThat(result).containsAll(expectedCurrencies);

    }
}
