package com.example.testassignmentapp.transaction;

import com.example.testassignmentapp.exchangerate.ExchangeRate;
import com.example.testassignmentapp.exchangerate.ExchangeRateService;
import com.example.testassignmentapp.currency.Currency;
import com.example.testassignmentapp.currency.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private CurrencyRepository currencyRepository;
    private ExchangeRateService exchangeRateService;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        currencyRepository = mock(CurrencyRepository.class);
        exchangeRateService = mock(ExchangeRateService.class);
        transactionService = new TransactionService(transactionRepository, exchangeRateService, currencyRepository);
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetTransactionsInPeriodInCurrency()  {
        TransactionReportRequest request = new TransactionReportRequest(
                LocalDate.of(2023, 2, 2),
                LocalDate.of(2023, 3, 2),
                "USD"
        );;
        Currency currency = new Currency("USD", "USD");
        List<ExchangeRate> exchangeRates = Arrays.asList(
                new ExchangeRate(
                        currency,
                        LocalDate.of(2023, 2, 10).atStartOfDay(),
                        BigDecimal.valueOf(80)),
                new ExchangeRate(
                        currency,
                        LocalDate.of(2023, 2, 12).atStartOfDay(),
                        BigDecimal.valueOf(82))
        );
        List<Transaction> transactions = createTransactions(100, 2023, 2, 10,11,12);

//        Map<Transaction, ExchangeRate> transactionToRate = new HashMap<>();
//        transactionToRate.put(transactions.get(0), exchangeRates.get(0));
//        transactionToRate.put(transactions.get(1), exchangeRates.get(0));
//        transactionToRate.put(transactions.get(2), exchangeRates.get(1));

        when(currencyRepository.findByCode(request.currency())).thenReturn(currency);
        when(exchangeRateService.getExchangeRatesForCurrencyInPeriod(request.from(), request.to(), currency)).thenReturn(exchangeRates);
        when(transactionRepository.findByDateBetweenOrderByDateAsc(request.from(), request.to())).thenReturn(transactions);

        List<TransactionDTO> transactionDTOs = transactionService.getTransactionsInPeriodInCurrency(request);

        assertThat(transactionDTOs).hasSize(3);
        assertThat(transactionDTOs.get(0).value().stripTrailingZeros())
                .isEqualByComparingTo(BigDecimal.valueOf(1.25));
        assertThat(transactionDTOs.get(1).value().stripTrailingZeros())
                .isEqualByComparingTo(BigDecimal.valueOf(1.25));
        assertThat(transactionDTOs.get(2).value().stripTrailingZeros())
                .isEqualByComparingTo(new BigDecimal("1.2195122").stripTrailingZeros());

    }

    @Test
    void testMapTransactionToRate() {
        Currency currency = new Currency("USD", "USD");

        List<Transaction> transactions = createTransactions(300, 2023, 3,10, 11, 12);
        List<ExchangeRate> rates = createRates(currency,80, 2023, 3,   10, 12);

        Map<Transaction, ExchangeRate> result = transactionService.mapTransactionToRate(transactions, rates);

        assertThat(result).hasSize(transactions.size());
        var sortedEntries = result.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getDate())).toList();

        assertThat(sortedEntries.get(0).getKey().getDate())
                .isEqualTo(LocalDate.of(2023, 3, 10));
        assertThat(sortedEntries.get(0).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 10).atStartOfDay());

        assertThat(sortedEntries.get(1).getKey().getDate())
                .isEqualTo(LocalDate.of(2023, 3, 11));
        assertThat(sortedEntries.get(1).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 10).atStartOfDay());

        assertThat(sortedEntries.get(2).getKey().getDate())
                .isEqualTo(LocalDate.of(2023, 3, 12));
        assertThat(sortedEntries.get(2).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 12).atStartOfDay());
    }

    @Test
    void testMapTransactionToRate2() {
        Currency currency = new Currency("USD", "USD");

        List<Transaction> transactions = createTransactions(300, 2023, 3,10, 11, 12);
        List<ExchangeRate> rates = createRates(currency,80, 2023, 3,   9);

        Map<Transaction, ExchangeRate> result = transactionService.mapTransactionToRate(transactions, rates);

        assertThat(result).hasSize(transactions.size());
        var sortedEntries = result.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getDate())).toList();

        assertThat(sortedEntries.get(0).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 9).atStartOfDay());

        assertThat(sortedEntries.get(1).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 9).atStartOfDay());

        assertThat(sortedEntries.get(2).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 9).atStartOfDay());
    }

    @Test
    void testMapTransactionToRate3() {
        Currency currency = new Currency("USD", "USD");

        List<Transaction> transactions = createTransactions(300, 2023, 3,10, 11, 12);
        List<ExchangeRate> rates = createRates(currency,80, 2023, 3,   6, 7, 9,  11, 14, 15);

        Map<Transaction, ExchangeRate> result = transactionService.mapTransactionToRate(transactions, rates);

        assertThat(result).hasSize(transactions.size());
        var sortedEntries = result.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getDate())).toList();

        assertThat(sortedEntries.get(0).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 9).atStartOfDay());

        assertThat(sortedEntries.get(1).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 11).atStartOfDay());

        assertThat(sortedEntries.get(2).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 11).atStartOfDay());
    }

    @Test
    void testMapTransactionToRate4() {
        Currency currency = new Currency("USD", "USD");

        List<Transaction> transactions = createTransactions(300, 2023, 3,
                10, 13, 15);
        List<ExchangeRate> rates = createRates(currency,80, 2023, 3,
                6, 7, 9, 11, 14, 17);

        Map<Transaction, ExchangeRate> result = transactionService.mapTransactionToRate(transactions, rates);

        assertThat(result).hasSize(transactions.size());
        var sortedEntries = result.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getDate())).toList();

        assertThat(sortedEntries.get(0).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 9).atStartOfDay());

        assertThat(sortedEntries.get(1).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 11).atStartOfDay());

        assertThat(sortedEntries.get(2).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 14).atStartOfDay());
    }

    @Test
    void testMapTransactionToRate5() {
        Currency currency = new Currency("USD", "USD");

        List<Transaction> transactions = createTransactions(300, 2023, 3,
                10, 11, 13, 14, 16);
        List<ExchangeRate> rates = createRates(currency,80, 2023, 3,
                6, 7, 9, 11, 14, 18, 20);

        Map<Transaction, ExchangeRate> result = transactionService.mapTransactionToRate(transactions, rates);

        assertThat(result).hasSize(transactions.size());
        var sortedEntries = result.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().getDate())).toList();

        assertThat(sortedEntries.get(0).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 9).atStartOfDay());

        assertThat(sortedEntries.get(1).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 11).atStartOfDay());

        assertThat(sortedEntries.get(2).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 11).atStartOfDay());

        assertThat(sortedEntries.get(3).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 14).atStartOfDay());

        assertThat(sortedEntries.get(3).getValue().getDatetime())
                .isEqualTo(LocalDate.of(2023, 3, 14).atStartOfDay());
    }

    @Test
    void testMapTransactionToRateWithInvalidInput() {
        Currency currency = new Currency("USD", "USD");

        List<Transaction> transactions =  createTransactions(300, 2022, 3, 10);
        List<ExchangeRate> rates = createRates(currency, 80, 2023, 3, 10);

        assertThrows(IllegalArgumentException.class, () -> transactionService.mapTransactionToRate(transactions, rates));
    }

    @Test
    void testCalculateTransactionInCurrency() {
        Transaction transactionInRoubles = new Transaction(
                LocalDate.of(2022, 3, 10), "test", BigDecimal.valueOf(1100));
        ExchangeRate exchangeRateForCurrency = new ExchangeRate(
                new Currency("USD", "USD"),
                LocalDate.of(2023, 3, 10).atStartOfDay(),
                BigDecimal.valueOf(80));

        TransactionDTO result = transactionService.calculateTransactionInCurrency(
                transactionInRoubles, exchangeRateForCurrency);

        assertThat(result.date()).isEqualTo(transactionInRoubles.getDate());
        assertThat(result.description()).isEqualTo(transactionInRoubles.getDescription());

        BigDecimal expectedValueInCurrency = BigDecimal.valueOf(13.75);
        assertThat(result.value()).isEqualByComparingTo(expectedValueInCurrency);
    }

    List<Transaction> createTransactions(int value, int year, int month, int... days) {
        return Arrays.stream(days)
                .mapToObj(i -> new Transaction(
                        LocalDate.of(year, month, i), "test", BigDecimal.valueOf(value)
                )).toList();
    }

    List<ExchangeRate> createRates(Currency currency, int value, int year, int month, int... days) {
        return Arrays.stream(days)
                .mapToObj(i ->
                        new ExchangeRate(
                                currency,
                                LocalDate.of(year, month, i).atStartOfDay(),
                                BigDecimal.valueOf(value))
                ).toList();
    }
}
