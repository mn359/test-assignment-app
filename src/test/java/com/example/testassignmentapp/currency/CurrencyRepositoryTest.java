package com.example.testassignmentapp.currency;


import com.example.testassignmentapp.exchangerate.currency.Currency;
import com.example.testassignmentapp.exchangerate.currency.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CurrencyRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CurrencyRepository currencyRepositoryRepository;

    @Test
    public void findCurrenciesByCodesTest() {
        currencyRepositoryRepository.saveAll(List.of(
                new Currency("USD", "USD"),
                new Currency("EUR", "EUR"),
                new Currency("RUB", "RUB")
        ));

        var res = currencyRepositoryRepository.findByCodeIn(List.of("EUR", "USD"));

        assertThat(res.size()).isEqualTo(2);
        assertThat(res.get(0).getCode()).isEqualTo("EUR");
        assertThat(res.get(1).getCode()).isEqualTo("USD");
    }

    @Test
    public void findCurrencyByCodeTest() {
        currencyRepositoryRepository.save(new Currency("TESTCURRENCY", "TESTCURRENCY"));

        var res = currencyRepositoryRepository.findByCode("TESTCURRENCY");

        assertThat(res).isNotNull();
        assertThat(res.getCode()).isEqualTo("TESTCURRENCY");
    }
}