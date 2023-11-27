package com.example.testassignmentapp.transaction;

import com.example.testassignmentapp.exchangerate.ExchangeRate;
import com.example.testassignmentapp.exchangerate.ExchangeRateService;
import com.example.testassignmentapp.currency.Currency;
import com.example.testassignmentapp.currency.CurrencyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;


@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    private CurrencyRepository currencyRepository;
    private ExchangeRateService exchangeRateService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              ExchangeRateService exchangeRateService,
                              CurrencyRepository currencyRepository) {
        this.transactionRepository = transactionRepository;
        this.exchangeRateService = exchangeRateService;
        this.currencyRepository = currencyRepository;
    }

    public void addTransaction(TransactionDTO dto) {
        Transaction transaction = new Transaction(
                dto.date(),
                dto.description(),
                dto.value()
        );
        transactionRepository.save(transaction);
    }

    public List<TransactionDTO> getTransactionsInPeriodInCurrency(TransactionReportRequest request) {
        Currency currency = currencyRepository.findByCode(request.currency());

        if (currency == null) {
            throw new EntityNotFoundException("Currency not found for code: " + request.currency());
        }

        List<ExchangeRate> exchangeRatesForCurrencyInPeriod = exchangeRateService
                .getExchangeRatesForCurrencyInPeriod(request.from(), request.to(), currency);

        List<Transaction> transactionsInPeriod = transactionRepository
                .findByDateBetweenOrderByDateAsc(request.from(), request.to());

        var transactionToRate= mapTransactionToRate(
                transactionsInPeriod,
                exchangeRatesForCurrencyInPeriod
        );

        return calculateTransactionsForRates(transactionToRate);
    }

    Map<Transaction, ExchangeRate> mapTransactionToRate(List<Transaction> transactions,
                                                        List<ExchangeRate> rates) {

        transactions = transactions.stream().sorted(Comparator.comparing(Transaction::getDate)).toList();
        rates = rates.stream().sorted(Comparator.comparing(ExchangeRate::getDatetime)).toList();

        Map<Transaction, ExchangeRate> transactionToRate = new HashMap<>();

        ListIterator<ExchangeRate> rateIterator = rates.listIterator();

        ExchangeRate rate;
        for (Transaction transaction : transactions) {
            rate = findExchangeRateForTransaction(rateIterator, transaction).orElseThrow(
                    () -> new  IllegalArgumentException("ExchangeRate not found for date " + transaction.getDate())
            );
            transactionToRate.put(transaction, rate);
        }
        
        return transactionToRate;
    }

    private Optional<ExchangeRate> findExchangeRateForTransaction(ListIterator<ExchangeRate> rateIterator,
                                                                  Transaction transaction) {
        while(rateIterator.hasNext()) {

            Optional<ExchangeRate> applicableProcessedRate = getExchangeRateApplicableForTransaction(
                    rateIterator, transaction
            );
            if (applicableProcessedRate.isPresent()) {
                return applicableProcessedRate;
            }
        }
        return Optional.empty();
    }

    private Optional<ExchangeRate> getExchangeRateApplicableForTransaction(ListIterator<ExchangeRate> rateIterator,
                                                                           Transaction transaction) {

        var currentlyProcessedRate = rateIterator.next();

        if (isRateApplicableForTransaction(currentlyProcessedRate, transaction)) {
            setPreviousIteratorValueForUnmappedRate(rateIterator);
            return Optional.of(currentlyProcessedRate);
        }
        return Optional.empty();
    }

    private boolean isRateApplicableForTransaction(ExchangeRate rate, Transaction transaction) {
        return !rate.getDatetime().isAfter(transaction.getDate().atStartOfDay());
    }

//    private Optional<ExchangeRate> findExchangeRateForTransactionNew(ListIterator<ExchangeRate> rateIterator,
//                                                                  Transaction transaction) {
//        return Stream.iterate(rateIterator, ListIterator::hasNext, ListIterator::next)
//                .filter(rate -> isRateApplicableForTransaction(rate.next(), transaction))
//                .peek(this::setPreviousIteratorValueForUnmappedRate)
//                .findFirst();
//    }

    private void setPreviousIteratorValueForUnmappedRate(ListIterator<ExchangeRate> rateIterator) {

        if (rateIterator.hasPrevious()) {
            rateIterator.previous();
        }
    }

    List<TransactionDTO> calculateTransactionsForRates(Map<Transaction, ExchangeRate> map) {
        return map.entrySet()
                .stream()
                .map(e -> calculateTransactionInCurrency(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(TransactionDTO::date))
                .toList();
    }

    TransactionDTO calculateTransactionInCurrency(Transaction transactionInRoubles, ExchangeRate exchangeRateForCurrency) {

        var trValueInRoubles= transactionInRoubles.getValue();
        if (trValueInRoubles.scale() < 8) {
            trValueInRoubles = trValueInRoubles.setScale(8);
        }

        var exchangeRateInRoubles = exchangeRateForCurrency.getRate();
        if (exchangeRateInRoubles.precision() < 8) {
            exchangeRateInRoubles = exchangeRateInRoubles.setScale(8);
        }

        var trValueInCurrency = trValueInRoubles.divide(exchangeRateInRoubles, RoundingMode.HALF_UP)
                .stripTrailingZeros();

        return new TransactionDTO(
                transactionInRoubles.getDate(),
                transactionInRoubles.getDescription(),
                trValueInCurrency
        );
    }
}
