package com.example.testassignmentapp.exchangerate;

import com.example.testassignmentapp.cbr.CbrWebService;
import com.example.testassignmentapp.currency.Currency;
import com.example.testassignmentapp.currency.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExchangeRateServiceTest {

    @Mock
    private CbrWebService cbrWebService;

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private CurrencyService currencyService;

    @InjectMocks
    private ExchangeRateService exchangeRateService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateExchangeRate() {
        Currency currency1 = new Currency("EUR", "978");
        Currency currency2 = new Currency("USD", "840");
        LocalDateTime localDateTime = LocalDate.of(2023, 1, 1).atStartOfDay();
        List<Currency> currencies = Arrays.asList(currency1, currency2);

        List<ExchangeRateDTO> data = Arrays.asList(
                new ExchangeRateDTO("EUR", "1.2", localDateTime),
                new ExchangeRateDTO("USD", "0.8", localDateTime)
        );

        List<ExchangeRate> expectedExchangeRates = Arrays.asList(
                new ExchangeRate(currency1, localDateTime,new BigDecimal("1.2")),
                new ExchangeRate(currency2, localDateTime, new BigDecimal("0.8"))
        );

        when(currencyService.findCurrenciesByCodes("EUR", "USD")).thenReturn(currencies);
        when(cbrWebService.getCurrentExchangeRates()).thenReturn(data);

        exchangeRateService.updateExchangeRate();

        verify(exchangeRateRepository, times(1)).saveAll(expectedExchangeRates);
    }


    @Test
    public void testGetExchangeRatesForCurrencyInPeriod() {
        Currency currency = new Currency("USD", "840");
        ExchangeRateDTO dto1 = new ExchangeRateDTO("USD", "1.2", LocalDate.of(2022, 1, 1).atStartOfDay());
        ExchangeRateDTO dto2 = new ExchangeRateDTO("USD", "1.4", LocalDate.of(2022, 1, 2).atStartOfDay());
        List<ExchangeRateDTO> exchangeRateDTOs = Arrays.asList(dto1, dto2);

        LocalDate from = LocalDate.of(2022, 1, 1);
        LocalDate to = LocalDate.of(2022, 1, 2);

        when(cbrWebService.getExchangeRatesForCurrencyInPeriod(from, to, currency.getCode(), currency.getInternalCbrCode()))
                .thenReturn(exchangeRateDTOs);

        List<ExchangeRate> expectedExchangeRates = Arrays.asList(
                new ExchangeRate(currency,  LocalDate.of(2022, 1, 1).atStartOfDay(), new BigDecimal("1.2")),
                new ExchangeRate(currency, LocalDate.of(2022, 1, 2).atStartOfDay(), new BigDecimal("1.4"))
        );

        List<ExchangeRate> actualExchangeRates = exchangeRateService.getExchangeRatesForCurrencyInPeriodFromCbr(from, to, currency);

        assertThat(expectedExchangeRates).isEqualTo(actualExchangeRates);
    }

    @Test
    public void testCreateExchangeRate() {
        Currency currency = new Currency("USD", "840");
        ExchangeRateDTO dto = new ExchangeRateDTO("USD", "1.2", LocalDate.of(2022, 1, 1).atStartOfDay());

        ExchangeRate expectedExchangeRate = new ExchangeRate(currency,   LocalDate.of(2022, 1, 1).atStartOfDay(), new BigDecimal("1.2"));
        ExchangeRate actualExchangeRate = exchangeRateService.createExchangeRate(currency, dto);

        assertThat(expectedExchangeRate).isEqualTo(actualExchangeRate);
    }
}