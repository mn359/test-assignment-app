package com.example.testassignmentapp.transaction;

import com.example.testassignmentapp.exchangerate.ExchangeRate;

import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionToRateMapper {

    private List<Transaction> transactions;
    private ListIterator<ExchangeRate> rateIterator;

    public TransactionToRateMapper(List<Transaction> transactions,
                                   List<ExchangeRate> rates) {
        this.transactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .toList();
        rates = rates.stream()
                .sorted(Comparator.comparing(ExchangeRate::getDatetime).reversed())
                .toList();
        this.rateIterator = rates.listIterator();
    }

    Map<Transaction, ExchangeRate> mapTransactionToRate() {
        return this.transactions.stream()
            .collect(Collectors.toMap(
                transaction -> transaction,
                transaction -> findExchangeRateForTransaction(transaction)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "ExchangeRate not found for date " + transaction.getDate())
                        )
            ));
    }

    private Optional<ExchangeRate> findExchangeRateForTransaction(Transaction transaction) {
        setPreviousIteratorValueToStartWithCurrentRate();

        return Stream.iterate(rateIterator, ListIterator::hasNext, (iterator) -> iterator)
                .map(iterator -> getExchangeRateApplicableForTransaction(transaction))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<ExchangeRate> getExchangeRateApplicableForTransaction(Transaction transaction) {
        var currentlyProcessedRate = rateIterator.next();

        return isRateApplicableForTransaction(currentlyProcessedRate, transaction)
                        ? Optional.of(currentlyProcessedRate)
                        : Optional.empty();
    }

    private boolean isRateApplicableForTransaction(ExchangeRate rate, Transaction transaction) {
        return !rate.getDatetime().isAfter(transaction.getDate().atStartOfDay());
    }

    private void setPreviousIteratorValueToStartWithCurrentRate() {
        if (rateIterator.hasPrevious()) {
            rateIterator.previous();
        }
    }
}
